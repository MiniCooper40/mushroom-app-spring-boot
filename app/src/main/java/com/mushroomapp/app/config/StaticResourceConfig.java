package com.mushroomapp.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    private static final String[] RESOURCE_LOCATIONS = {
            "/static/"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        System.out.println("in addResourceHandlers");
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations(RESOURCE_LOCATIONS);
    }
}
