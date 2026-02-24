package com.nexusenroll.enrollment.client;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class CourseDTO {
    private String courseCode;
    private String courseName;
    private Integer capacity;
    private Integer currentEnrollment;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<String> prerequisiteCodes;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
}
