package com.nexusenroll.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue enrollmentQueue() {
        return new Queue("enrollmentQueue", true);
    }
}
