package org.example.notificationservice.mapper;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.events.UserEvent;
import org.springframework.stereotype.Component;

@Component
public final class NotificationMapper {

    public NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.email(),
                notification.operation()
        );
    }

    public Notification fromController(CreateNotificationRequest request) {
        return new Notification(request.email(), request.operation());
    }

    public Notification fromProducer(UserEvent userEvent) {
        return new Notification(userEvent.getEmail(), userEvent.getOperation());
    }
}

