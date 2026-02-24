package com.nexusenroll.student.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String studentId; // User-facing ID (e.g., S12345)

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String degreeProgram; // e.g., "Computer Science", "Business"

    private Integer totalCredits;

    @ElementCollection
    @CollectionTable(name = "student_grades", joinColumns = @JoinColumn(name = "student_id"))
    @MapKeyColumn(name = "course_code")
    @Column(name = "grade")
    private Map<String, String> courseGrades; // Map of course code to grade

    // Simple list of required courses for the degree (hardcoded for now or fetched
    // from Program entity)
    @Transient
    public List<String> getRequiredCourses() {
        if ("Computer Science".equalsIgnoreCase(degreeProgram)) {
            return List.of("CS101", "CS102", "CS201", "SCS4221");
        } else if ("Business".equalsIgnoreCase(degreeProgram)) {
            return List.of("BUS101", "BUS102", "ECON101");
        }
        return List.of();
    }
}
