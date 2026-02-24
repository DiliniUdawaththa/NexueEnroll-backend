package com.nexusenroll.enrollment.controller;

import com.nexusenroll.enrollment.model.Enrollment;
import com.nexusenroll.enrollment.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/{studentId:.+}/{courseCode:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<Enrollment> enroll(@PathVariable String studentId, @PathVariable String courseCode) {
        return ResponseEntity.ok(enrollmentService.enrollStudent(studentId, courseCode));
    }

    @DeleteMapping("/{studentId:.+}/{courseCode:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    public ResponseEntity<Void> drop(@PathVariable String studentId, @PathVariable String courseCode) {
        enrollmentService.dropCourse(studentId, courseCode);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/student/{studentId:.+}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(@PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsForStudent(studentId));
    }

    @GetMapping("/course/{courseCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<Enrollment>> getCourseRoster(@PathVariable String courseCode) {
        return ResponseEntity.ok(enrollmentService.getCourseRoster(courseCode));
    }

    @PostMapping("/grades")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<Void> submitGrades(@RequestBody List<Enrollment> grades) {
        enrollmentService.submitGrades(grades);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/force/{studentId:.+}/{courseCode:.+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Enrollment> forceEnroll(@PathVariable String studentId, @PathVariable String courseCode) {
        return ResponseEntity.ok(enrollmentService.forceEnroll(studentId, courseCode));
    }
}
