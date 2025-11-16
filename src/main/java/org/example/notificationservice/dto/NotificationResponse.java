package org.example.notificationservice.dto;

import org.example.notificationservice.events.OperationType;

public record NotificationResponse(
        String email,
        OperationType operation
) {
}

