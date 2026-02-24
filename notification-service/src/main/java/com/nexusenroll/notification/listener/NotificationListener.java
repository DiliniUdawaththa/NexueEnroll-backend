package com.nexusenroll.notification.listener;

import com.nexusenroll.notification.dto.EnrollmentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationListener {

    private static final String QUEUE_NAME = "enrollmentQueue";

    @RabbitListener(queues = QUEUE_NAME)
    public void handleEnrollmentEvent(EnrollmentEvent event) {
        log.info("Received Enrollment Event: {}", event);
        sendNotification(event);
    }

    private void sendNotification(EnrollmentEvent event) {
        if ("CONFIRMED".equals(event.getStatus())) {
            log.info("SENT NOTIFICATION: Student {} has been enrolled in Course {}.",
                    event.getStudentId(), event.getCourseCode());
        } else if ("CANCELLED".equals(event.getStatus())) {
            log.info("SENT NOTIFICATION: Student {} has dropped Course {}.",
                    event.getStudentId(), event.getCourseCode());
            log.info("AUTOMATED ALERT: Spot opened up in Course {}! Notifying waitlisted students...",
                    event.getCourseCode());
        }
    }
}
