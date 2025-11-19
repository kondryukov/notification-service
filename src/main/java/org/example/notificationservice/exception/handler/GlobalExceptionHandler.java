package org.example.notificationservice.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.example.notificationservice.events.OperationType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleBodyValidation(MethodArgumentNotValidException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setDetail("Validation failed");
        problemDetail.setType(URI.create("http://localhost:8081/notification/error/bad-request"));
        List<Map<String, String>> list = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            Map<String, String> field = Map.of("field", fieldError.getField(), "message", fieldError.getDefaultMessage());
            list.add(field);
        }
        problemDetail.setProperty("errors", list);
        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ProblemDetail handleInvalidBody(HttpMessageNotReadableException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("http://localhost:8081/notification/error/bad-request"));
        if (exception.getCause() instanceof InvalidFormatException ife && ife.getTargetType() == OperationType.class) {
            problemDetail.setDetail("Incorrect format");
            List<Map<String, String>> list = new ArrayList<>();
            Map<String, String> field = Map.of("field", "operation", "message", "operation must be 'CREATE' or 'DELETE");
            list.add(field);
            problemDetail.setProperty("errors", list);
        } else {
            problemDetail.setDetail("Damaged JSON request");
        }
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ProblemDetail handleUnexpected() {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setDetail("Unexpected error");
        problemDetail.setType(URI.create("http://localhost:8081/notification/error/internal-server"));
        return problemDetail;
    }
}
