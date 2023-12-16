package com.mushroomapp.app.controller;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface HttpRequestReader {
    String getId(HttpServletRequest request);
}
