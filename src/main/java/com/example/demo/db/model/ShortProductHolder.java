package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortProductHolder implements ModelHolder {

    private ObjectNode value;

    public String getId() { return value.get("id").asText("-"); }

}
