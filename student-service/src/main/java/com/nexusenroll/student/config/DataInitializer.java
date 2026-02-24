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

            Student s1 = Student.builder()
                    .studentId("kasun.p")
                    .firstName("Kasun")
                    .lastName("Perera")
                    .email("kasun.p@nexusenroll.edu")
                    .degreeProgram("Software Engineering")
                    .totalCredits(3)
                    .courseGrades(new HashMap<>() {{ put("CS100", "A"); }})
                    .build();

            Student s2 = Student.builder()
                    .studentId("dilini.s")
                    .firstName("Dilini")
                    .lastName("Silva")
                    .email("dilini.s@nexusenroll.edu")
                    .degreeProgram("Computer Science")
                    .totalCredits(33)
                    .courseGrades(new HashMap<>() {{ put("CS100", "A-"); put("MAT101", "B+"); }})
                    .build();

            Student s3 = Student.builder()
                    .studentId("nimal.j")
                    .firstName("Nimal")
                    .lastName("Jayasekara")
                    .email("nimal.j@nexusenroll.edu")
                    .degreeProgram("Information Technology")
                    .totalCredits(18)
                    .courseGrades(new HashMap<>() {{ put("CS100", "B"); }})
                    .build();

            Student s4 = Student.builder()
                    .studentId("tharushi.f")
                    .firstName("Tharushi")
                    .lastName("Fernando")
                    .email("tharushi.f@nexusenroll.edu")
                    .degreeProgram("Artificial Intelligence")
                    .totalCredits(48)
                    .courseGrades(new HashMap<>() {{ put("CS100", "A"); put("CS101", "B"); }})
                    .build();

            studentRepository.saveAll(Arrays.asList(s1, s2, s3, s4));
            log.info("Successfully seeded 4 Sri Lankan students matching Auth users.");
        } else {
            log.info("Student database already contains data. Skipping seeding.");
        }
    }
}
