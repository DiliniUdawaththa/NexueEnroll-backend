package com.nexusenroll.course.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String courseCode; // e.g., CS101

    @Column(nullable = false)
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String instructorName;

    private Long instructorId;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer currentEnrollment;

    // Time schedule for conflict check (simplified)
    private String dayOfWeek; // MONDAY, TUESDAY...
    private LocalTime startTime;
    private LocalTime endTime;

    @ElementCollection
    @CollectionTable(name = "course_prerequisites", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "prerequisite_code")
    private List<String> prerequisiteCodes;
}
