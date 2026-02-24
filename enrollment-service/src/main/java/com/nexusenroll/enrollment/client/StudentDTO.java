package com.nexusenroll.enrollment.client;

import lombok.Data;

import java.util.List;

@Data
public class StudentDTO {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private List<String> completedCourseCodes;
}
