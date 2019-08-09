package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayDeque;
import java.util.Collection;

@Data
public class ProductNode {

    private ShortProductHolder value;
    private LongProductHolder description;

    public ProductNode(ShortProductHolder value) {
        this.value = value;
    }

    public ObjectNode toJson() {
        ObjectNode root = value.getValue();
        if (description != null) {
            root.set("longDescription", description.getValue());
        }
        return root;
    }
}
