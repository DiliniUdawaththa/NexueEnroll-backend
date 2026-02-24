package com.nexusenroll.enrollment.repository;

import com.nexusenroll.enrollment.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(String studentId);

    Optional<Enrollment> findByStudentIdAndCourseCode(String studentId, String courseCode);

    List<Enrollment> findByCourseCode(String courseCode);

    List<Enrollment> findByCourseCodeAndStatusOrderByEnrollmentDateAsc(String courseCode,
            Enrollment.EnrollmentStatus status);
}
