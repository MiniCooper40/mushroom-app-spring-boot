package com.mushroomapp.app.deserialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mushroomapp.app.controller.format.deserialize.AiInsightDeserializer;
import com.mushroomapp.app.model.insight.AiInsight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeserializeInsightsTests {

    @Autowired
    private AiInsightDeserializer aiInsightDeserializer;

    @Test
    public void deserialize() throws IOException {
        File file = new File("C:\\Users\\ratbo\\Documents\\Code\\SpringBoot\\mushroom-v1\\app\\src\\test\\java\\com\\mushroomapp\\app\\deserialize\\insight1.txt");

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(AiInsight.class, aiInsightDeserializer);
        objectMapper.registerModule(module);

        AiInsight i = objectMapper.readValue(file, AiInsight.class);

        System.out.println("got ai insights: \n" + i);
    }
}
