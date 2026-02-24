package com.nexusenroll.enrollment.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String username = request.getHeader("loggedInUser");
                String roles = request.getHeader("roles");

                if (username != null) {
                    requestTemplate.header("loggedInUser", username);
                }
                if (roles != null) {
                    requestTemplate.header("roles", roles);
                }
            }
        };
    }
}
