package com.example.demo.entity;

public class ProductNode implements CatalogNode {
    private ProductDefinition definition;

    public ProductNode(ProductDefinition definition) {
        this.definition = definition;
    }
}
