package com.mushroomapp.app.controller;

import jakarta.servlet.http.HttpServletRequest;

public class FirebaseRequestReader implements HttpRequestReader {

    @Override
    public String getId(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
