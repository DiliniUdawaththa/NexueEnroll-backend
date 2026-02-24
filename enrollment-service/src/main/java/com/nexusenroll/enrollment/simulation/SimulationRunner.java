package com.nexusenroll.enrollment.simulation;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Profile("simulation")
public class SimulationRunner implements CommandLineRunner {

    // Helper class to represent requests
    private record StudentRequest(String studentId, String firstName, String lastName, String email) {}
    private record CourseRequest(String courseCode, String courseName, Integer capacity, Integer currentEnrollment) {}

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=========================================");
        System.out.println("STARTING NEXUS ENROLL SIMULATION");
        System.out.println("=========================================");

        RestTemplate restTemplate = new RestTemplate();
        String studentServiceUrl = "http://localhost:8081/api/students";
        String courseServiceUrl = "http://localhost:8082/api/courses";
        String enrollmentServiceUrl = "http://localhost:8083/api/enrollments";

        try {
            // 1. Create Student
            System.out.println("1. Creating Student (S001)...");
            StudentRequest student = new StudentRequest("S001", "John", "Doe", "john@example.com");
            try {
                restTemplate.postForObject(studentServiceUrl, student, Object.class);
                System.out.println("   -> Student created successfully.");
            } catch (Exception e) {
                System.out.println("   -> Student extraction/creation failed (might already exist).");
            }

            // 2. Create Course (Capacity 1)
            System.out.println("2. Creating Course (CS101) with Capacity 1...");
            CourseRequest course = new CourseRequest("CS101", "Intro to Java", 1, 0);
             try {
                restTemplate.postForObject(courseServiceUrl, course, Object.class);
                System.out.println("   -> Course created successfully.");
            } catch (Exception e) {
                System.out.println("   -> Course extraction/creation failed (might already exist).");
            }

            // 3. Enroll Student (Should Succeed)
            System.out.println("3. Enrolling S001 in CS101...");
            try {
                restTemplate.postForObject(enrollmentServiceUrl + "/S001/CS101", null, Object.class);
                System.out.println("   -> SUCCESS: Student enrolled. Notification should be sent.");
            } catch (Exception e) {
                 System.out.println("   -> FAILED: " + e.getMessage());
            }

            // 4. Create Another Student
            System.out.println("4. Creating Student (S002)...");
            StudentRequest student2 = new StudentRequest("S002", "Jane", "Smith", "jane@example.com");
             try {
                restTemplate.postForObject(studentServiceUrl, student2, Object.class);
                System.out.println("   -> Student created successfully.");
            } catch (Exception e) {
                System.out.println("   -> Student extraction/creation failed.");
            }

            // 5. Enroll Second Student (Should Fail - Capacity)
            System.out.println("5. Enrolling S002 in CS101 (Should fail due to capacity)...");
            try {
                restTemplate.postForObject(enrollmentServiceUrl + "/S002/CS101", null, Object.class);
                System.out.println("   -> ERROR: Enrollment succeeded but should have failed!");
            } catch (Exception e) {
                System.out.println("   -> SUCCESS: Enrollment failed as expected (Capacity Reached).");
            }

        } catch (Exception e) {
            System.err.println("Simulation Error: " + e.getMessage());
            System.err.println("Make sure all services (Student, Course, Enrollment) are running!");
        }

        System.out.println("=========================================");
        System.out.println("SIMULATION COMPLETE");
        System.out.println("=========================================");
    }
}
