package com.example.demo.entity;

import com.example.demo.contentHouse.api.PageItems;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
public class SubCategory {
    @Id
    private String id;
    private String name;
    private String categoryId;
    private String path;
    private List<PageItems> children;
    private boolean leaf;
    private String productsCount;
    private String weight;
    private String parentId;


    public SubCategory(String id,
                       String name,
                       String categoryId,
                       String path,
                       boolean leaf,
                       String productsCount,
                       String weight,
                       String parentId,
                       List<PageItems> children) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.path = path;
        this.leaf = leaf;
        this.productsCount = productsCount;
        this.weight = weight;
        this.parentId = parentId;
        this.children = children;

    }
}
