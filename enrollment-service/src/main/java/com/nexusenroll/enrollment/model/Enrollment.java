package com.nexusenroll.enrollment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "enrollments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private String courseCode;

    @Column(nullable = false)
    private LocalDateTime enrollmentDate;

    @Enumerated(EnumType.STRING)
    private EnrollmentStatus status;

    private String grade;

    @Enumerated(EnumType.STRING)
    private GradeStatus gradeStatus;

    public enum EnrollmentStatus {
        PENDING, CONFIRMED, REJECTED, CANCELLED, WAITLISTED
    }

    public enum GradeStatus {
        PENDING, SUBMITTED, APPROVED
    }
}
