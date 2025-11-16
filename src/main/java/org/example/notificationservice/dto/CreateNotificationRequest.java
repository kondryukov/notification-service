package org.example.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.notificationservice.events.OperationType;

public record CreateNotificationRequest(
        @NotBlank
        @Email
        @Size(max = 254)
        String email,

        @NotNull(message = "operation is required and must be 'CREATE' or 'DELETE'")
        OperationType operation
) {
}