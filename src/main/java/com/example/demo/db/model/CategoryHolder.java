package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryHolder implements ModelHolder {

    private ObjectNode value;

    public int getId() { return value.get("id").asInt(-1); }

    public boolean isLeaf() { return value.get("leaf").asBoolean(false); }

    public int getProductsCount() { return value.get("productsCount").asInt(0); }
}
