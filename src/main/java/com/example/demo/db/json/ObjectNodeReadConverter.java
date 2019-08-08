package com.example.demo.db.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
@ReadingConverter
public class ObjectNodeReadConverter implements Converter<Document, ObjectNode> {

    private final ObjectMapper objectMapper;

    public ObjectNodeReadConverter(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }

    @Override
    public ObjectNode convert(Document source) {
        try {
            ObjectNode json = objectMapper.readValue(source.toJson(), ObjectNode.class);
            json.remove("_id");
            return json;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}