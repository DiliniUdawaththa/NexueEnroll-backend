package com.nexusenroll.enrollment.service;

import com.nexusenroll.enrollment.client.CourseClient;
import com.nexusenroll.enrollment.client.CourseDTO;
import com.nexusenroll.enrollment.client.StudentClient;
import com.nexusenroll.enrollment.client.StudentDTO;
import com.nexusenroll.enrollment.config.RabbitMQConfig;
import com.nexusenroll.enrollment.exception.CourseFullException;
import com.nexusenroll.enrollment.model.Enrollment;
import com.nexusenroll.enrollment.repository.EnrollmentRepository;
import com.nexusenroll.enrollment.strategy.EnrollmentValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;
    private final CourseClient courseClient;
    private final List<EnrollmentValidationStrategy> validationStrategies;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Enrollment enrollStudent(String studentId, String courseCode) {
        // 1. Fetch Data
        StudentDTO student = studentClient.getStudent(studentId);
        CourseDTO course = courseClient.getCourse(courseCode);
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudentId(studentId);

        // 2. Check for Existing Enrollment
        if (enrollmentRepository.findByStudentIdAndCourseCode(studentId, courseCode).isPresent()) {
            throw new RuntimeException("Student already enrolled in this course.");
        }

        Enrollment.EnrollmentStatus initialStatus = Enrollment.EnrollmentStatus.CONFIRMED;

        // 3. Execute Validation Strategies (Strategy Pattern)
        try {
            for (EnrollmentValidationStrategy strategy : validationStrategies) {
                strategy.validate(student, course, existingEnrollments);
            }
        } catch (CourseFullException e) {
            // Course is full, but other validations passed, so we can waitlist
            initialStatus = Enrollment.EnrollmentStatus.WAITLISTED;
        }

        // 4. Create and Save Enrollment
        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseCode(courseCode)
                .enrollmentDate(LocalDateTime.now())
                .status(initialStatus)
                .build();
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        // 5. Update Course Capacity if confirmed
        if (initialStatus == Enrollment.EnrollmentStatus.CONFIRMED) {
            courseClient.incrementEnrollment(courseCode);
        }

        // 6. Publish Event (Observer Pattern via RabbitMQ)
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, savedEnrollment);

        return savedEnrollment;
    }

    public List<Enrollment> getEnrollmentsForStudent(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    @Transactional
    public void dropCourse(String studentId, String courseCode) {
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseCode(studentId, courseCode)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        boolean wasConfirmed = enrollment.getStatus() == Enrollment.EnrollmentStatus.CONFIRMED;
        enrollment.setStatus(Enrollment.EnrollmentStatus.CANCELLED);
        enrollmentRepository.save(enrollment);

        if (wasConfirmed) {
            // Decrement capacity
            courseClient.decrementEnrollment(courseCode);

            // Promote first waitlisted student
            Optional<Enrollment> nextInLine = enrollmentRepository.findByCourseCodeAndStatusOrderByEnrollmentDateAsc(
                    courseCode, Enrollment.EnrollmentStatus.WAITLISTED).stream().findFirst();

            if (nextInLine.isPresent()) {
                Enrollment promoted = nextInLine.get();
                promoted.setStatus(Enrollment.EnrollmentStatus.CONFIRMED);
                enrollmentRepository.save(promoted);
                courseClient.incrementEnrollment(courseCode);

                // Notify the promoted student
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, promoted);
            }
        }

        // Notify drop
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, enrollment);
    }

    public List<Enrollment> getCourseRoster(String courseCode) {
        return enrollmentRepository.findByCourseCode(courseCode);
    }

    @Transactional
    public void submitGrades(List<Enrollment> gradeSubmissions) {
        for (Enrollment submission : gradeSubmissions) {
            Enrollment enrollment = enrollmentRepository
                    .findByStudentIdAndCourseCode(submission.getStudentId(), submission.getCourseCode())
                    .orElseThrow(() -> new RuntimeException(
                            "Enrollment not found for student " + submission.getStudentId()));

            enrollment.setGrade(submission.getGrade());
            enrollment.setGradeStatus(Enrollment.GradeStatus.SUBMITTED);
            enrollmentRepository.save(enrollment);
        }
    }

    @Transactional
    public Enrollment forceEnroll(String studentId, String courseCode) {
        // Admin override: No validation except existing enrollment
        if (enrollmentRepository.findByStudentIdAndCourseCode(studentId, courseCode).isPresent()) {
            throw new RuntimeException("Student already enrolled");
        }

        Enrollment enrollment = Enrollment.builder()
                .studentId(studentId)
                .courseCode(courseCode)
                .enrollmentDate(LocalDateTime.now())
                .status(Enrollment.EnrollmentStatus.CONFIRMED)
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);
        courseClient.incrementEnrollment(courseCode);
        return saved;
    }
}
