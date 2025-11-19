package org.example.notificationservice.mapper;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.events.OperationType;
import org.example.notificationservice.events.UserEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationMapperTest {

    private final NotificationMapper notificationMapper = new NotificationMapper();

    @Test
    void fromController() {
        CreateNotificationRequest request = new CreateNotificationRequest("name@mail.ru", OperationType.CREATE);
        Notification notification = notificationMapper.fromController(request);

        assertThat(notification.email()).isEqualTo("name@mail.ru");
        assertThat(notification.operation()).isEqualTo(OperationType.CREATE);
    }

    @Test
    void fromProducer() {
        UserEvent userEvent = new UserEvent();
        userEvent.setEmail("name@mail.ru");
        userEvent.setOperation(OperationType.DELETE);
        Notification notification = notificationMapper.fromProducer(userEvent);

        assertThat(notification.email()).isEqualTo("name@mail.ru");
        assertThat(notification.operation()).isEqualTo(OperationType.DELETE);

    }

    @Test
    void toResponse() {
        Notification notification = new Notification("name@mail.ru", OperationType.DELETE);
        NotificationResponse notificationResponse = notificationMapper.toResponse(notification);

        assertThat(notificationResponse).isEqualTo(new NotificationResponse("name@mail.ru", OperationType.DELETE));
    }
}