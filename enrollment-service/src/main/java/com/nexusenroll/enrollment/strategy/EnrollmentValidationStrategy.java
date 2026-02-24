package com.nexusenroll.enrollment.strategy;

import com.nexusenroll.enrollment.client.CourseDTO;
import com.nexusenroll.enrollment.client.StudentDTO;
import com.nexusenroll.enrollment.model.Enrollment;

import java.util.List;

/**
 * Strategy Pattern Interface: Defines the contract for enrollment validation
 * strategies.
 * Concrete implementations will encapsulate specific validation logic (SOLID:
 * Open/Closed Principle).
 */
public interface EnrollmentValidationStrategy {
    void validate(StudentDTO student, CourseDTO course, List<Enrollment> existingEnrollments);
}
