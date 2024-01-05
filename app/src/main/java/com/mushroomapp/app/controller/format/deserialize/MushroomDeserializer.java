package com.mushroomapp.app.controller.format.deserialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mushroomapp.app.model.mushroom.Mushroom;
import com.mushroomapp.app.service.MushroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MushroomDeserializer extends JsonDeserializer<Mushroom> {

    @Autowired
    private MushroomService mushroomService;

    @Override
    public Mushroom deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return null;
    }
}
