package com.mushroomapp.app.controller.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class ErrorMessage {
    public String cause = "";
}
