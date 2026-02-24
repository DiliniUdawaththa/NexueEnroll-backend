package com.nexusenroll.gateway.config;

import com.nexusenroll.gateway.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Autowired
    private AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("student-service", r -> r.path("/api/students/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://student-service"))
                .route("course-service", r -> r.path("/api/courses/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://course-service"))
                .route("enrollment-service", r -> r.path("/api/enrollments/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://enrollment-service"))
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://auth-service"))
                .build();
    }
}
