package com.nexusenroll.student.config;

import com.nexusenroll.student.model.Student;
import com.nexusenroll.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            log.info("Seeding initial student data...");

            Map<String, String> grades1 = new HashMap<>();
            grades1.put("CS100", "A");

            Student s1 = Student.builder()
                    .studentId("S1001")
                    .firstName("Kasun")
                    .lastName("Perera")
                    .email("kasun.p@nexusenroll.edu")
                    .degreeProgram("Software Engineering")
                    .totalCredits(3)
                    .courseGrades(grades1)
                    .build();

            Map<String, String> grades2 = new HashMap<>();
            grades2.put("CS100", "A-");
            grades2.put("MAT101", "B+");

            Student s2 = Student.builder()
                    .studentId("S1002")
                    .firstName("Dilini")
                    .lastName("Silva")
                    .email("dilini.s@nexusenroll.edu")
                    .degreeProgram("Computer Science")
                    .totalCredits(33)
                    .courseGrades(grades2)
                    .build();

            Map<String, String> grades3 = new HashMap<>();
            grades3.put("CS100", "B");

            Student s3 = Student.builder()
                    .studentId("S1003")
                    .firstName("Nimal")
                    .lastName("Jayasekara")
                    .email("nimal.j@nexusenroll.edu")
                    .degreeProgram("Information Technology")
                    .totalCredits(18)
                    .courseGrades(grades3)
                    .build();

            Map<String, String> grades4 = new HashMap<>();
            grades4.put("CS100", "A");
            grades4.put("CS101", "B");

            Student s4 = Student.builder()
                    .studentId("S1004")
                    .firstName("Tharushi")
                    .lastName("Fernando")
                    .email("tharushi.f@nexusenroll.edu")
                    .degreeProgram("Artificial Intelligence")
                    .totalCredits(48)
                    .courseGrades(grades4)
                    .build();

            studentRepository.saveAll(Arrays.asList(s1, s2, s3, s4));
            log.info("Successfully seeded 4 Sri Lankan students.");
        } else {
            log.info("Student database already contains data. Skipping seeding.");
        }
    }
}
