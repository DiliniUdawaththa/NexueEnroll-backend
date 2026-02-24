package com.nexusenroll.enrollment.strategy;

import com.nexusenroll.enrollment.client.CourseDTO;
import com.nexusenroll.enrollment.client.StudentDTO;
import com.nexusenroll.enrollment.model.Enrollment;
import com.nexusenroll.enrollment.client.CourseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

/**
 * Strategy Implementation: Validates time conflicts.
 * (Placeholder logic for POC, assuming simple overlapping check could be added
 * here)
 */
@Component
@RequiredArgsConstructor
public class TimeConflictValidationStrategy implements EnrollmentValidationStrategy {

    private final CourseClient courseClient;

    @Override
    public void validate(StudentDTO student, CourseDTO course, List<Enrollment> existingEnrollments) {
        if (course.getStartTime() == null || course.getEndTime() == null || course.getDayOfWeek() == null) {
            return; // No schedule set, assume no conflict
        }

        for (Enrollment existing : existingEnrollments) {
            // Only check against confirmed enrollments
            if (existing.getStatus() == Enrollment.EnrollmentStatus.CONFIRMED) {
                try {
                    CourseDTO existingCourse = courseClient.getCourse(existing.getCourseCode());

                    if (existingCourse != null &&
                            existingCourse.getDayOfWeek() != null &&
                            existingCourse.getDayOfWeek().equalsIgnoreCase(course.getDayOfWeek())) {

                        // Check overlap: (StartA < EndB) and (StartB < EndA)
                        if (isOverlapping(course.getStartTime(), course.getEndTime(),
                                existingCourse.getStartTime(), existingCourse.getEndTime())) {
                            throw new RuntimeException("Validation Failed: Time Conflict. " + course.getCourseCode() +
                                    " overlaps with " + existingCourse.getCourseCode() + " on "
                                    + course.getDayOfWeek());
                        }
                    }
                } catch (RuntimeException e) {
                    if (e.getMessage().contains("Validation Failed"))
                        throw e;
                    System.err
                            .println("Could not fetch course details for conflict check: " + existing.getCourseCode());
                }
            }
        }
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        if (start1 == null || end1 == null || start2 == null || end2 == null)
            return false;
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
