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

            // Kasun, Dilini, Nimal enrolled in CS101 (Capacity 3)
            Enrollment e1 = Enrollment.builder()
                    .studentId("kasun.p")
                    .courseCode("CS101")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            Enrollment e2 = Enrollment.builder()
                    .studentId("dilini.s")
                    .courseCode("CS101")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            Enrollment e3 = Enrollment.builder()
                    .studentId("nimal.j")
                    .courseCode("CS101")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            // Tharushi waitlisted for CS101
            Enrollment e4 = Enrollment.builder()
                    .studentId("tharushi.f")
                    .courseCode("CS101")
                    .enrollmentDate(LocalDateTime.now().plusMinutes(5))
                    .status(Enrollment.EnrollmentStatus.WAITLISTED)
                    .build();

            // Dilini also in CS202
            Enrollment e5 = Enrollment.builder()
                    .studentId("dilini.s")
                    .courseCode("CS202")
                    .enrollmentDate(LocalDateTime.now())
                    .status(Enrollment.EnrollmentStatus.CONFIRMED)
                    .build();

            enrollmentRepository.saveAll(Arrays.asList(e1, e2, e3, e4, e5));
            log.info("Successfully seeded 5 enrollments (4 for CS101, 1 for CS202).");
        }
    }
}
