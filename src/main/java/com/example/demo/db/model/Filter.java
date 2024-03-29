package com.example.demo.db.model;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    private Map<Integer, JsonNode> groups;


}
