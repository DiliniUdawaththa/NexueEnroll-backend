package com.nexusenroll.enrollment.config;

import com.nexusenroll.enrollment.model.Enrollment;
import com.nexusenroll.enrollment.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final EnrollmentRepository enrollmentRepository;

    @Override
    public void run(String... args) {
        if (enrollmentRepository.count() == 0) {
            log.info("Seeding initial enrollment data...");

            // Kasun (S1001) enrolled in CS101
            Enrollment e1 = Enrollment.builder()
                    .studentId("S1001")
                    .courseCode("CS101")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            // Dilini (S1002) enrolled in CS101 and CS202
            Enrollment e2 = Enrollment.builder()
                    .studentId("S1002")
                    .courseCode("CS101")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            Enrollment enrollment3 = Enrollment.builder()
                    .studentId("S1002")
                    .courseCode("CS202")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            enrollmentRepository.saveAll(Arrays.asList(e1, e2, enrollment3));
            log.info("Successfully seeded 3 enrollments.");
        }
    }
}
