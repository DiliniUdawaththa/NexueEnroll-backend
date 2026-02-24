package com.nexusenroll.course.config;

import com.nexusenroll.course.model.Course;
import com.nexusenroll.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CourseRepository courseRepository;

    @Override
    public void run(String... args) {
        if (courseRepository.count() == 0) {
            log.info("Seeding initial course data...");

            Course c1 = Course.builder()
                    .courseCode("CS101")
                    .courseName("Object Oriented Programming with Java")
                    .description("Learn OOP concepts using Java.")
                    .department("Software Engineering")
                    .instructorName("Prof. Rohan Gunawardena")
                    .instructorId(201L)
                    .capacity(60)
                    .currentEnrollment(0)
                    .dayOfWeek("MONDAY")
                    .startTime(LocalTime.of(8, 30))
                    .endTime(LocalTime.of(10, 30))
                    .prerequisiteCodes(Collections.emptyList())
                    .build();

            Course c2 = Course.builder()
                    .courseCode("CS202")
                    .courseName("Data Structures and Algorithms")
                    .description("Fundamental structures and algorithm analysis.")
                    .department("Computer Science")
                    .instructorName("Dr. Priyantha Silva")
                    .instructorId(202L)
                    .capacity(50)
                    .currentEnrollment(0)
                    .dayOfWeek("TUESDAY")
                    .startTime(LocalTime.of(13, 0))
                    .endTime(LocalTime.of(15, 0))
                    .prerequisiteCodes(Arrays.asList("CS101"))
                    .build();

            Course c3 = Course.builder()
                    .courseCode("DB301")
                    .courseName("Relational Database Systems")
                    .description("Design and SQL for relational databases.")
                    .department("Information Systems")
                    .instructorName("Mrs. Kumari Perera")
                    .instructorId(203L)
                    .capacity(40)
                    .currentEnrollment(0)
                    .dayOfWeek("WEDNESDAY")
                    .startTime(LocalTime.of(10, 30))
                    .endTime(LocalTime.of(12, 30))
                    .prerequisiteCodes(Collections.emptyList())
                    .build();

            Course c4 = Course.builder()
                    .courseCode("AI401")
                    .courseName("Artificial Intelligence Foundations")
                    .description("Introduction to search, logic, and learning.")
                    .department("Computer Science")
                    .instructorName("Dr. Sanath Jayasuriya")
                    .instructorId(204L)
                    .capacity(30)
                    .currentEnrollment(0)
                    .dayOfWeek("THURSDAY")
                    .startTime(LocalTime.of(14, 0))
                    .endTime(LocalTime.of(16, 0))
                    .prerequisiteCodes(Arrays.asList("CS202"))
                    .build();

            courseRepository.saveAll(Arrays.asList(c1, c2, c3, c4));
            log.info("Successfully seeded 4 courses with Sri Lankan faculty.");
        } else {
            log.info("Course database already contains data. Skipping seeding.");
        }
    }
}
