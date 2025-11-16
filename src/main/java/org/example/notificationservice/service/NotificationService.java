package org.example.notificationservice.service;

import jakarta.validation.Valid;
import org.example.notificationservice.domain.Notification;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.events.OperationType;
import org.example.notificationservice.events.UserEvent;
import org.example.notificationservice.mapper.NotificationMapper;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationMapper notificationMapper;
    public final JavaMailSender emailSender;

    private final String createReplica = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
    private final String deletedReplica = "Здравствуйте! Ваш аккаунт был удалён.";

    public NotificationService(JavaMailSender emailSender, NotificationMapper notificationMapper) {
        this.emailSender = emailSender;
        this.notificationMapper = notificationMapper;
    }

    public NotificationResponse sendEmail(@Valid CreateNotificationRequest request) {
        Notification notification = notificationMapper.fromController(request);

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notification.email());
        simpleMailMessage.setSubject(notification.operation().toString());
        simpleMailMessage.setText(notification.operation().equals(OperationType.CREATE) ? createReplica : deletedReplica);
        emailSender.send(simpleMailMessage);

        return notificationMapper.toResponse(notification);
    }

    public void sendEmail(UserEvent userEvent) {
        Notification notification = notificationMapper.fromProducer(userEvent);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(notification.email());
        simpleMailMessage.setSubject(notification.operation().toString());
        simpleMailMessage.setText(notification.operation().equals(OperationType.CREATE) ? createReplica : deletedReplica);
        emailSender.send(simpleMailMessage);
    }
}
