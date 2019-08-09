package com.example.demo.db;

import com.example.demo.db.json.ObjectNodeReadConverter;
import com.example.demo.db.json.ObjectNodeWriteConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.Arrays;

@Configuration
public class MongoConfig {

    @Autowired
    private ObjectMapper mapper;

    @Bean
    public MongoCustomConversions jacksonObjectNodeToBSONConversions() {
        return new MongoCustomConversions(Arrays.asList(
                new ObjectNodeReadConverter(mapper),
                new ObjectNodeWriteConverter()));
    }
}
