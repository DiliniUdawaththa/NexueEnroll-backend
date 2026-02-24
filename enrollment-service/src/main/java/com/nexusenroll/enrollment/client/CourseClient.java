package com.nexusenroll.enrollment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.time.LocalTime;
import java.util.List;
import lombok.Data;

@FeignClient(name = "course-service")
public interface CourseClient {
    @GetMapping("/api/courses/{courseCode}")
    CourseDTO getCourse(@PathVariable("courseCode") String courseCode);

    @PostMapping("/api/courses/{courseCode}/increment")
    void incrementEnrollment(@PathVariable("courseCode") String courseCode);

    @PostMapping("/api/courses/{courseCode}/decrement")
    void decrementEnrollment(@PathVariable("courseCode") String courseCode);
}
