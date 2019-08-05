package com.example.demo.entity;

import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class CategoryNode implements CatalogNode {
    private CategoryDefinition definition;
    private List<CatalogNode> children;

    public CategoryNode(CategoryDefinition definition) {
        this.definition = definition;
        this.children = Collections.emptyList();
    }

    public void append(CatalogNode node) {
        children.add(node);
    }
}
