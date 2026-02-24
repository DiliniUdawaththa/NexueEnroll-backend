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
            userRepository.save(User.builder()
                    .username("rohan.g")
                    .password(passwordEncoder.encode("faculty123"))
                    .roles(Set.of("ROLE_FACULTY"))
                    .build());
            
            userRepository.save(User.builder()
                    .username("priyantha.s")
                    .password(passwordEncoder.encode("faculty123"))
                    .roles(Set.of("ROLE_FACULTY"))
                    .build());

            // Students
            userRepository.save(User.builder()
                    .username("kasun.p")
                    .password(passwordEncoder.encode("student123"))
                    .roles(Set.of("ROLE_STUDENT"))
                    .build());

            userRepository.save(User.builder()
                    .username("dilini.s")
                    .password(passwordEncoder.encode("student123"))
                    .roles(Set.of("ROLE_STUDENT"))
                    .build());

            log.info("Successfully seeded 5 users (1 admin, 2 faculty, 2 students).");
        }
    }
}
