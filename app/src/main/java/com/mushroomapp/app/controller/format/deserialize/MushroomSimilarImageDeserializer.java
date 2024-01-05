package com.mushroomapp.app.controller.format.deserialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mushroomapp.app.model.insight.MushroomSimilarImage;
import com.mushroomapp.app.service.AiInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MushroomSimilarImageDeserializer extends JsonDeserializer<MushroomSimilarImage> {

    @Autowired
    private AiInsightService aiInsightService;

    @Override
    public MushroomSimilarImage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return null;
    }
}
