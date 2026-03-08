package com.nexusenroll.auth.service;

import com.nexusenroll.auth.dto.AuthDto.*;
import com.nexusenroll.auth.model.User;
import com.nexusenroll.auth.repository.UserRepository;
import com.nexusenroll.auth.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        String password = request.getPassword();
        if (password == null || password.isEmpty()) {
            password = "faculty123"; // Default for faculty
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(password))
                .roles(request.getRoles())
                .active(true)
                .build();

        userRepository.save(user);
    }

    public java.util.List<User> getAllFaculty() {
        return userRepository.findAll().stream()
                .filter(u -> u.getRoles().contains("ROLE_FACULTY"))
                .collect(Collectors.toList());
    }

    public User updateUser(Long id, User details) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(details.getUsername());
        // Password is not updated as per requirements
        return userRepository.save(user);
    }

    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive());
        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isActive()) {
            throw new RuntimeException("Account is deactivated. Please contact administrator.");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String username = authentication.getName();
        Set<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        String token = jwtUtils.generateToken(username, roles.stream().toList());

        return new AuthResponse(token, username, roles);
    }
}
