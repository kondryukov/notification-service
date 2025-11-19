package org.example.notificationservice.service;

import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.events.OperationType;
import org.example.notificationservice.events.UserEvent;
import org.example.notificationservice.mapper.NotificationMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    NotificationMapper mapper;

    @InjectMocks
    private NotificationService service;

    @Test
    void sendEmailFromRequestCreate() {
        Notification notification = new Notification("name@mail.ru", OperationType.CREATE);
        var request = new CreateNotificationRequest("name@mail.ru", OperationType.CREATE);

        when(mapper.fromController(request)).thenReturn(notification);
        when(mapper.toResponse(notification))
                .thenReturn(new NotificationResponse(request.email(), request.operation()));

        NotificationResponse response = service.sendEmail(request);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage simpleMailMessage = captor.getValue();
        assertThat(simpleMailMessage.getTo()).containsExactly("name@mail.ru");
        assertThat(simpleMailMessage.getSubject()).isEqualTo(OperationType.CREATE.toString());
        assertThat(simpleMailMessage.getText()).isEqualTo("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");

        assertThat(response.email()).isEqualTo("name@mail.ru");
        assertThat(response.operation()).isEqualTo(OperationType.CREATE);

        verify(mapper).fromController(request);
        verify(mapper).toResponse(notification);
    }

    @Test
    void sendEmailFromRequestDelete() {
        Notification notification = new Notification("name@mail.ru", OperationType.DELETE);
        var request = new CreateNotificationRequest("name@mail.ru", OperationType.DELETE);

        when(mapper.fromController(request)).thenReturn(notification);
        when(mapper.toResponse(notification))
                .thenReturn(new NotificationResponse(request.email(), request.operation()));

        NotificationResponse response = service.sendEmail(request);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage simpleMailMessage = captor.getValue();
        assertThat(simpleMailMessage.getTo()).containsExactly("name@mail.ru");
        assertThat(simpleMailMessage.getSubject()).isEqualTo(OperationType.DELETE.toString());
        assertThat(simpleMailMessage.getText()).isEqualTo("Здравствуйте! Ваш аккаунт был удалён.");

        assertThat(response.email()).isEqualTo("name@mail.ru");
        assertThat(response.operation()).isEqualTo(OperationType.DELETE);

        verify(mapper).fromController(request);
        verify(mapper).toResponse(notification);
    }

    @Test
    void sendEmailFromUserEventCreate() {
        UserEvent userEvent = new UserEvent();
        userEvent.setEmail("name@mail.ru");
        userEvent.setOperation(OperationType.CREATE);
        Notification notification = new Notification("name@mail.ru", OperationType.CREATE);
        when(mapper.fromProducer(userEvent)).thenReturn(notification);
        service.sendEmail(userEvent);

        UserEvent sentEvent = new UserEvent();
        sentEvent.setEmail("name@mail.ru");
        sentEvent.setOperation(OperationType.CREATE);
        assertThat(sentEvent).isEqualTo(userEvent);

        assertThat(sentEvent).isEqualTo(sentEvent);
        assertThat(userEvent).isNotEqualTo(notification);
        assertThat(userEvent).isNotEqualTo(null);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());
        SimpleMailMessage simpleMailMessage = captor.getValue();
        assertThat(simpleMailMessage.getTo()).containsExactly("name@mail.ru");
        assertThat(simpleMailMessage.getSubject()).isEqualTo(OperationType.CREATE.toString());
        assertThat(simpleMailMessage.getText()).isEqualTo("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.");
    }

    @Test
    void sendEmailFromUserEventDelete() {
        UserEvent userEvent = new UserEvent();
        userEvent.setEmail("name@mail.ru");
        userEvent.setOperation(OperationType.DELETE);
        Notification notification = new Notification("name@mail.ru", OperationType.DELETE);
        when(mapper.fromProducer(userEvent)).thenReturn(notification);
        service.sendEmail(userEvent);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());
        SimpleMailMessage simpleMailMessage = captor.getValue();

        assertThat(simpleMailMessage.getTo()).containsExactly("name@mail.ru");
        assertThat(simpleMailMessage.getSubject()).isEqualTo(OperationType.DELETE.toString());
        assertThat(simpleMailMessage.getText()).isEqualTo("Здравствуйте! Ваш аккаунт был удалён.");
    }
}