package com.mushroomapp.app.controller.format.deserialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mushroomapp.app.model.insight.MushroomClassificationSuggestion;
import com.mushroomapp.app.service.AiInsightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MushroomClassificationSuggestionDeserializer extends JsonDeserializer<MushroomClassificationSuggestion> {

    @Autowired
    private AiInsightService aiInsightService;

    @Autowired
    private MushroomSimilarImageDeserializer mushroomSimilarImageDeserializer;

    @Override
    public MushroomClassificationSuggestion deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return null;
    }
}
