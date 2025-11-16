package org.example.notificationservice.messaging;

import org.example.notificationservice.events.UserEvent;
import org.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationKafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(NotificationKafkaConsumer.class);
    private final NotificationService notificationService;

    public NotificationKafkaConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "users", groupId = "notification-group")
    public void consume(UserEvent userEvent) {
        log.info("Consuming user {}", userEvent.getEmail());
        notificationService.sendEmail(userEvent);
    }
}
