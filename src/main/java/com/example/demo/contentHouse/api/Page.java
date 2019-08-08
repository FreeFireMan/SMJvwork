package com.example.demo.contentHouse.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;

import java.util.List;

@Data
public class Page {
    private List<ObjectNode> pageItems;
    private int totalRows;
    private String error;
    private boolean success;
}
