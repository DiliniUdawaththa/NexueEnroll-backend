package com.nexusenroll.enrollment.strategy;

import com.nexusenroll.enrollment.client.CourseDTO;
import com.nexusenroll.enrollment.client.StudentDTO;
import com.nexusenroll.enrollment.exception.CourseFullException;
import com.nexusenroll.enrollment.model.Enrollment;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Strategy Implementation: Validates course capacity.
 */
@Component
public class CapacityValidationStrategy implements EnrollmentValidationStrategy {

    @Override
    public void validate(StudentDTO student, CourseDTO course, List<Enrollment> existingEnrollments) {
        if (course.getCurrentEnrollment() >= course.getCapacity()) {
            throw new CourseFullException("Course " + course.getCourseCode() + " is full.");
        }
    }
}
