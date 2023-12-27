package com.mushroomapp.app.controller.format.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.mushroomapp.app.model.storage.Directory;
import com.mushroomapp.app.model.storage.Media;

import java.io.IOException;

public class SerializeMediaToPath extends JsonSerializer<Media> {
    @Override
    public void serialize(Media media, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String filename = media.getFilename();
        System.out.println("filename is " + filename);
        String directoryPath = media.getDirectory().getPath();
        System.out.println("directory is " + directoryPath);

        jsonGenerator.writeString(directoryPath+filename);
    }
}
