package com.nexusenroll.course.service;

import com.nexusenroll.course.model.Course;
import com.nexusenroll.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseByCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseCode));
    }

    public Course createCourse(Course course) {
        if (course.getCurrentEnrollment() == null) {
            course.setCurrentEnrollment(0);
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found: " + id));

        course.setCourseName(courseDetails.getCourseName());
        course.setDescription(courseDetails.getDescription());
        course.setDepartment(courseDetails.getDepartment());
        course.setInstructorName(courseDetails.getInstructorName());
        course.setInstructorId(courseDetails.getInstructorId());
        course.setCapacity(courseDetails.getCapacity());
        course.setDayOfWeek(courseDetails.getDayOfWeek());
        course.setStartTime(courseDetails.getStartTime());
        course.setEndTime(courseDetails.getEndTime());
        course.setPrerequisiteCodes(courseDetails.getPrerequisiteCodes());

        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public List<Course> searchByDepartment(String department) {
        return courseRepository.findByDepartment(department);
    }

    public List<Course> searchByInstructor(String instructorName) {
        return courseRepository.findByInstructorNameContainingIgnoreCase(instructorName);
    }

    public List<Course> searchByKeyword(String keyword) {
        return courseRepository.searchByKeyword(keyword);
    }

    @Transactional
    public void incrementEnrollment(String courseCode) {
        Course course = getCourseByCode(courseCode);
        if (course.getCurrentEnrollment() >= course.getCapacity()) {
            throw new RuntimeException("Course capacity full");
        }
        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        courseRepository.save(course);
    }

    @Transactional
    public void decrementEnrollment(String courseCode) {
        Course course = getCourseByCode(courseCode);
        if (course.getCurrentEnrollment() > 0) {
            course.setCurrentEnrollment(course.getCurrentEnrollment() - 1);
            courseRepository.save(course);
        }
    }

    public List<Course> getOverCapacityCourses() {
        return courseRepository.findAll().stream()
                .filter(c -> c.getCurrentEnrollment() >= (c.getCapacity() * 0.9))
                .toList();
    }

    public Map<String, Integer> getEnrollmentStatsByDepartment() {
        return courseRepository.findAll().stream()
                .collect(Collectors.groupingBy(Course::getDepartment,
                        Collectors.summingInt(Course::getCurrentEnrollment)));
    }

    public Map<String, Long> getFacultyWorkload() {
        return courseRepository.findAll().stream()
                .collect(Collectors.groupingBy(Course::getInstructorName, Collectors.counting()));
    }
}
