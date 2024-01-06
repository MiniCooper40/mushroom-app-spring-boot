package com.mushroomapp.app.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.mushroomapp.app.model.insight.AiInsight;
import com.mushroomapp.app.model.insight.MushroomClassificationSuggestion;
import com.mushroomapp.app.model.insight.MushroomSimilarImage;
import com.mushroomapp.app.repository.AiInsightRepository;
import com.mushroomapp.app.repository.LikeRepository;
import com.mushroomapp.app.repository.MushroomClassificationSuggestionRepository;
import com.mushroomapp.app.repository.MushroomSimilarImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AiInsightService {

    @Autowired
    private AiInsightRepository aiInsightRepository;

    @Autowired
    private MushroomClassificationSuggestionRepository mushroomClassificationSuggestionRepository;

    @Autowired
    private MushroomSimilarImageRepository mushroomSimilarImageRepository;

    private final ObjectMapper mapper = new ObjectMapper();

    JsonFactory jsonFactory = mapper.getFactory();

    public AiInsight createInsight(MultipartFile file) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI("123123.com"))
                .GET()
                .build();

        HttpClient http = HttpClient.newHttpClient();
        HttpResponse<String> response = http.send(request, HttpResponse.BodyHandlers.ofString());

        JsonParser jsonParser = this.jsonFactory.createParser(response.body());
        JsonNode jsonNode = mapper.readTree(jsonParser);

        return null;
    }

    public AiInsight save(AiInsight aiInsight) {
        return this.aiInsightRepository.save(aiInsight);
    }

    public MushroomSimilarImage save(MushroomSimilarImage similarImage) {
        return this.mushroomSimilarImageRepository.save(similarImage);
    }

    public MushroomClassificationSuggestion save(MushroomClassificationSuggestion classificationSuggestion) {
        return this.mushroomClassificationSuggestionRepository.save(classificationSuggestion);
    }

}
