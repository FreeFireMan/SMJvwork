package com.example.demo.contentHouse.model;

import com.example.demo.contentHouse.api.PageItem;
import com.example.demo.db.model.CategoryNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Document
@Data
@NoArgsConstructor
public class CategoryDefinition {

    @Id
    private String id;
    private String name;
    private String categoryId;
    private String path;
    private boolean leaf;
    private int productsCount;
    private String weight;
    private Optional<String> parentId;

    public CategoryDefinition(
        String id,
        String name,
        String categoryId,
        String path,
        boolean leaf,
        int productsCount,
        String weight,
        Optional<String> parentId) {

        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.path = path;
        this.leaf = leaf;
        this.productsCount = productsCount;
        this.weight = weight;
        this.parentId = parentId;
    }

    public CategoryNode toNode() {
        return new CategoryNode(id, name, categoryId, path, weight);
    }
}
