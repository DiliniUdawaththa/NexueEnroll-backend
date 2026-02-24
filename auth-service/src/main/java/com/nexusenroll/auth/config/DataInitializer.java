package com.nexusenroll.auth.config;

import com.nexusenroll.auth.model.User;
import com.nexusenroll.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            log.info("Seeding initial user data for auth-service...");

            // Admin
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of("ROLE_ADMIN"))
                    .build());

            // Faculty
            String[] faculty = {"rohan.g", "priyantha.s", "kumari.p", "sanath.j"};
            for (String f : faculty) {
                userRepository.save(User.builder()
                        .username(f)
                        .password(passwordEncoder.encode("faculty123"))
                        .roles(Set.of("ROLE_FACULTY"))
                        .build());
            }

            // Students
            String[] students = {"kasun.p", "dilini.s", "nimal.j", "tharushi.f"};
            for (String s : students) {
                userRepository.save(User.builder()
                        .username(s)
                        .password(passwordEncoder.encode("student123"))
                        .roles(Set.of("ROLE_STUDENT"))
                        .build());
            }

            log.info("Successfully seeded users (1 admin, 4 faculty, 4 students).");
        }
    }
}
