package com.mushroomapp.app.controller.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class EntityDoesNotExistException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(
            value = {NoSuchElementException.class}
    )
    public ResponseEntity<Object> handleNoSuchElement(NoSuchElementException exception, WebRequest request) {
        ErrorMessage errorMessage = ErrorMessage
                .builder()
                .cause(exception.getMessage())
                .build();

        return handleExceptionInternal(
                exception,
                errorMessage,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(
            value = {SQLException.class}
    )
    public ResponseEntity<Object> handleSqlException(SQLException exception, WebRequest request) {
        ErrorMessage errorMessage = ErrorMessage
                .builder()
                .cause(exception.getMessage())
                .build();

        return handleExceptionInternal(
                exception,
                errorMessage,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request
        );
    }

}
