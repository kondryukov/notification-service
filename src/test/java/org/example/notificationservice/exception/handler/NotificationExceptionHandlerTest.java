package org.example.notificationservice.exception.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleUnexpectedError() {
        ProblemDetail problemDetail = handler.handleUnexpected();

        assertThat(problemDetail.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(problemDetail.getDetail()).isEqualTo("Unexpected error");
        assertThat(problemDetail.getType()).isEqualTo(URI.create("http://localhost:8081/users/error/internal-server"));
    }
}
