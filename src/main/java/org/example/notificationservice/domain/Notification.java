package org.example.notificationservice.domain;

import org.example.notificationservice.events.OperationType;

public record Notification(String email, OperationType operation) {
}
