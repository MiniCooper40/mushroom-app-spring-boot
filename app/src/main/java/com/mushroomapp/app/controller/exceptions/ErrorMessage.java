package com.mushroomapp.app.controller.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public class ErrorMessage {

    @Builder.Default
    public LocalDateTime timestamp = LocalDateTime.now();

    public String cause = "";
}
