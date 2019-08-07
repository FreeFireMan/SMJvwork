package com.example.demo.db.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayDeque;
import java.util.Collection;

@Data
public class CategoryNode implements CatalogNode {

    @Id
    private String id;
    private String name;
    private String categoryId;
    private String path;
    private String weight;
    private Collection<CatalogNode> children = new ArrayDeque<CatalogNode>();

    public CategoryNode(String id, String name, String categoryId, String path, String weight) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.path = path;
        this.weight = weight;
    }

    public void append(CatalogNode node) {
        children.add(node);
    }
}
