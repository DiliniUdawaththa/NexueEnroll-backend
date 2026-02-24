package com.nexusenroll.enrollment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import lombok.Data;
import java.util.List;

@FeignClient(name = "student-service")
public interface StudentClient {
    @GetMapping("/api/students/{studentId}")
    StudentDTO getStudent(@PathVariable("studentId") String studentId);
}

