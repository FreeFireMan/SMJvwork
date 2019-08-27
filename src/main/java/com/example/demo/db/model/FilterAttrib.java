package com.example.demo.db.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;
import java.util.Set;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterAttrib {

    private Map<String, Set<String>> attributes;
}
