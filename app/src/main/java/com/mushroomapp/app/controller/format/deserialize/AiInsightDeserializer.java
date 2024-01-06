package com.mushroomapp.app.controller.format.deserialize;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.mushroomapp.app.model.insight.AiInsight;
import com.mushroomapp.app.model.insight.MushroomClassificationSuggestion;
import com.mushroomapp.app.model.insight.MushroomSimilarImage;
import com.mushroomapp.app.model.mushroom.Mushroom;
import com.mushroomapp.app.model.mushroom.MushroomDescription;
import com.mushroomapp.app.service.AiInsightService;
import com.mushroomapp.app.service.MushroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class AiInsightDeserializer extends JsonDeserializer<AiInsight> {

    @Autowired
    private AiInsightService aiInsightService;

    @Autowired
    private MushroomService mushroomService;

    @Override
    public AiInsight deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode rootNode = jsonParser.readValueAsTree();

        JsonNode resultNode = rootNode.get("result");
        JsonNode classificationNode = resultNode.get("classification");
        JsonNode suggestionNodes = classificationNode.get("suggestions");

        List<AiInsightResult> results = deserializeMushroomClassificationSuggestions(suggestionNodes);

        AiInsight aiInsight = new AiInsight();

        this.aiInsightService.save(aiInsight);

        for(AiInsightResult result : results) {
            MushroomClassificationSuggestion suggestion = result.getMushroomClassificationSuggestion();
            suggestion.setAiInsight(aiInsight);
            this.aiInsightService.save(suggestion);
            for(MushroomSimilarImage similarImage : result.getMushroomSimilarImageList()) {
                similarImage.setClassificationSuggestion(suggestion);
                this.aiInsightService.save(similarImage);
                suggestion.addSimilarImage(similarImage);
            }
            aiInsight.addClassificationSuggestion(suggestion);
        }

        return aiInsight;
    }


    private List<AiInsightResult> deserializeMushroomClassificationSuggestions(JsonNode jsonNode) {

        if(!jsonNode.isArray()) throw new IllegalArgumentException("no suggestions array present");

        List<AiInsightResult> results = new LinkedList<>();
        for(JsonNode suggestion : jsonNode) results.add(
                deserializeMushroomClassificationSuggestion(suggestion)
        );

        return results;
    }

    private AiInsightResult  deserializeMushroomClassificationSuggestion(JsonNode jsonNode) {

        float probability = jsonNode.get("probability").floatValue();

        JsonNode similarImagesNode = jsonNode.get("similar_images");
        List<MushroomSimilarImage> similarImages = deserializeMushroomSimilarImages(similarImagesNode);

        Mushroom mushroom = deserializeMushroom(jsonNode);

        MushroomClassificationSuggestion classificationSuggestion = MushroomClassificationSuggestion
                .builder()
                .mushroom(mushroom)
                .probability(probability)
                .build();

        System.out.println("Got classification with id " + classificationSuggestion.getId());

        return AiInsightResult
                .builder()
                .mushroomClassificationSuggestion(classificationSuggestion)
                .mushroomSimilarImageList(similarImages)
                .build();
    }

    private List<MushroomSimilarImage> deserializeMushroomSimilarImages(JsonNode jsonNode) {

        if(!jsonNode.isArray()) throw new IllegalArgumentException("no suggestions array present");

        List<MushroomSimilarImage> similarImages = new LinkedList<>();
        for(JsonNode similarImage : jsonNode) similarImages.add(deserializeMushroomSimilarImage(similarImage));

        System.out.println("found " + similarImages.size() + " similar images");

        return similarImages;
    }

    private MushroomSimilarImage  deserializeMushroomSimilarImage(JsonNode jsonNode) {

        String id = jsonNode.get("id").asText();
        String url = jsonNode.get("url").asText();
        String licenseName = jsonNode.get("license_name").asText();
        String licenseUrl = jsonNode.get("license_url").asText();
        String citation = jsonNode.get("citation").asText();
        float similarity = jsonNode.get("similarity").floatValue();
        String urlSmall = jsonNode.get("url_small").asText();

        return MushroomSimilarImage
                .builder()
                .id(id)
                .url(url)
                .licenseName(licenseName)
                .licenseUrl(licenseUrl)
                .citation(citation)
                .similarity(similarity)
                .urlSmall(urlSmall)
                .build();
    }

    private Mushroom deserializeMushroom(JsonNode jsonNode) {

        String id = jsonNode.get("id").asText();

        Optional<Mushroom> optionalMushroom = this.mushroomService.getReferenceById(id);
        if(optionalMushroom.isPresent()) return optionalMushroom.get();

        String scientificName = jsonNode.get("name").asText();
        System.out.println("got mushroom with name: " + scientificName);

        JsonNode details = jsonNode.get("details");

        List<String> commonNames = new LinkedList<>();
        for(JsonNode nameNode : details.get("common_names")) commonNames.add(nameNode.asText());

        String edibility = details.get("edibility").asText();
        String psychoactive = details.get("psychoactive").asText();

        JsonNode descriptionNode = details.get("description");
        MushroomDescription description = deserializeMushroomDescription(descriptionNode);


        Mushroom mushroom =  Mushroom
                .builder()
                .commonNames(commonNames)
                .description(description)
                .edibility(edibility)
                .scientificName(scientificName)
                .description(description)
                .id(id)
                .build();

        return this.mushroomService.save(mushroom);
    }

    private MushroomDescription deserializeMushroomDescription(JsonNode jsonNode) {
        String text = jsonNode.get("value").asText();
        String citation = jsonNode.get("citation").asText();
        String licenseName = jsonNode.get("license_name").asText();
        String licenseUrl = jsonNode.get("license_url").asText();

        MushroomDescription description = MushroomDescription
                .builder()
                .text(text)
                .citation(citation)
                .licenseName(licenseName)
                .licenseUrl(licenseUrl)
                .build();

        return this.mushroomService.save(description);
    }
}
