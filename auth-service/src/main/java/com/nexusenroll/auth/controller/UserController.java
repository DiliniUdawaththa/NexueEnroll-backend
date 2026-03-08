package com.nexusenroll.auth.controller;

import com.nexusenroll.auth.dto.AuthDto.RegisterRequest;
import com.nexusenroll.auth.model.User;
import com.nexusenroll.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private AuthService authService;

    @GetMapping("/faculty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllFaculty() {
        return ResponseEntity.ok(authService.getAllFaculty());
    }

    @PostMapping("/faculty")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addFaculty(@RequestBody RegisterRequest request) {
        request.setRoles(Set.of("ROLE_FACULTY"));
        authService.register(request);
        return ResponseEntity.ok("Faculty added successfully");
    }

    @PutMapping("/faculty/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateFaculty(@PathVariable Long id, @RequestBody User details) {
        return ResponseEntity.ok(authService.updateUser(id, details));
    }

    @PatchMapping("/faculty/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggleFacultyStatus(@PathVariable Long id) {
        authService.toggleUserStatus(id);
        return ResponseEntity.ok().build();
    }
}
