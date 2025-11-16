package org.example.notificationservice;

import org.example.notificationservice.events.OperationType;
import org.example.notificationservice.events.UserEvent;
import org.example.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.ConfluentKafkaContainer;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class NotificationKafkaConsumerIT {

    @Container
    static ConfluentKafkaContainer kafka = new ConfluentKafkaContainer("confluentinc/cp-kafka:7.6.1");

    @DynamicPropertySource
    public static void initKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    public KafkaTemplate<String, UserEvent> kafkaTemplate;

    @MockitoBean
    NotificationService notificationService;

    @Test
    void consumeMessage() {
        UserEvent userEvent = new UserEvent();
        userEvent.setEmail("name@mail.ru");
        userEvent.setOperation(OperationType.CREATE);

        kafkaTemplate.send("users", userEvent);

        verify(notificationService, timeout(5000)).sendEmail(userEvent);
    }
}