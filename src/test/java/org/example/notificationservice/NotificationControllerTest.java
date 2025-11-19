package org.example.notificationservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.notificationservice.controller.NotificationController;
import org.example.notificationservice.dto.CreateNotificationRequest;
import org.example.notificationservice.dto.NotificationResponse;
import org.example.notificationservice.events.OperationType;
import org.example.notificationservice.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    private NotificationService notificationService;

    @Test
    void createUserNotification() throws Exception {
        NotificationResponse response = new NotificationResponse(
                "name@mail.ru", OperationType.CREATE
        );

        when(notificationService.sendEmail(ArgumentMatchers.any(CreateNotificationRequest.class))).thenReturn(response);

        var request = new CreateNotificationRequest("name@mail.ru", OperationType.CREATE);

        mockMvc.perform(post("/notification/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.operation").value(OperationType.CREATE.name()))
                .andExpect(jsonPath("$.email").value("name@mail.ru"));
    }

    @Test
    void createNotificationWrongOperation() throws Exception {
        String body = """
                {
                  "email": "name@mail.ru",
                  "operation": "wrongOperation"
                }
                """;

        mockMvc.perform(post("/notification/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.detail").value("Incorrect format"));
    }

    @Test
    void createNotificationInvalidEmail() throws Exception {
        var request = new CreateNotificationRequest("wrongEmail", OperationType.CREATE);
        mockMvc.perform(post("/notification/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.detail").value("Validation failed"));
    }

    @Test
    void damagedJSON() throws Exception {
        String body = """
                {
                    email: name@mail.ru
                }
                """;

        mockMvc.perform(post("/notification/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.detail").value("Damaged JSON request"));
    }
}