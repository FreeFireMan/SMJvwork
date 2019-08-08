package com.example.demo.db.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

@Component
@WritingConverter
public class ObjectNodeWriteConverter implements Converter<ObjectNode, Document> {

    @Override
    public Document convert(ObjectNode source) {
        Document bson = Document.parse(source.toString());
        return bson;
    }
}