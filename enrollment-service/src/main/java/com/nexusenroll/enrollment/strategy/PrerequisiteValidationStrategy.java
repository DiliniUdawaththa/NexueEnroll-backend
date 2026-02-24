package com.nexusenroll.enrollment.strategy;

import com.nexusenroll.enrollment.client.CourseDTO;
import com.nexusenroll.enrollment.client.StudentDTO;
import com.nexusenroll.enrollment.model.Enrollment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Strategy Implementation: Validates prerequisites.
 */
@Component
public class PrerequisiteValidationStrategy implements EnrollmentValidationStrategy {

    @Override
    public void validate(StudentDTO student, CourseDTO course, List<Enrollment> existingEnrollments) {
        List<String> requiredPrerequisites = course.getPrerequisiteCodes();
        if (requiredPrerequisites == null || requiredPrerequisites.isEmpty()) {
            return;
        }

        List<String> completedCourses = student.getCompletedCourseCodes();
        for (String prereq : requiredPrerequisites) {
            if (completedCourses == null || !completedCourses.contains(prereq)) {
                throw new RuntimeException("Validation Failed: Prerequisite " + prereq + " not met.");
            }
        }
    }
}
