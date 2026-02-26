package com.nexusenroll.course.controller;

import com.nexusenroll.course.model.Course;
import com.nexusenroll.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<Course>> getMyCourses() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return ResponseEntity.ok(courseService.getCoursesByInstructorUsername(username));
    }

    @GetMapping("/{courseCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<Course> getCourseByCode(@PathVariable String courseCode) {
        return ResponseEntity.ok(courseService.getCourseByCode(courseCode));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/department")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<List<Course>> searchByDepartment(@RequestParam String department) {
        return ResponseEntity.ok(courseService.searchByDepartment(department));
    }

    @GetMapping("/search/instructor")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<List<Course>> searchByInstructor(@RequestParam String instructor) {
        return ResponseEntity.ok(courseService.searchByInstructor(instructor));
    }

    @GetMapping("/search/keyword")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<List<Course>> searchByKeyword(@RequestParam String keyword) {
        return ResponseEntity.ok(courseService.searchByKeyword(keyword));
    }

    @GetMapping("/reports/over-capacity")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<Course>> getOverCapacityCourses() {
        return ResponseEntity.ok(courseService.getOverCapacityCourses());
    }

    @GetMapping("/reports/stats/department")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<Map<String, Integer>> getEnrollmentStatsByDepartment() {
        return ResponseEntity.ok(courseService.getEnrollmentStatsByDepartment());
    }

    @GetMapping("/reports/stats/workload")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<Map<String, Long>> getFacultyWorkload() {
        return ResponseEntity.ok(courseService.getFacultyWorkload());
    }

    @PostMapping("/{courseCode}/increment")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<Void> incrementEnrollment(@PathVariable String courseCode) {
        courseService.incrementEnrollment(courseCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{courseCode}/decrement")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY', 'STUDENT')")
    public ResponseEntity<Void> decrementEnrollment(@PathVariable String courseCode) {
        courseService.decrementEnrollment(courseCode);
        return ResponseEntity.ok().build();
    }
}
