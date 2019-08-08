package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayDeque;
import java.util.Collection;

@Data
@AllArgsConstructor
public class ShortProductNode {

    private ShortProductHolder value;

    public ObjectNode toJson() {
        return value.getValue();
    }
}
