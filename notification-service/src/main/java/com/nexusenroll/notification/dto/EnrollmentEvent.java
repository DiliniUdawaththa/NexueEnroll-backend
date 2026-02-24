package com.nexusenroll.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EnrollmentEvent {
    private Long id;
    private String studentId;
    private String courseCode;
    private LocalDateTime enrollmentDate;
    private String status;
}
