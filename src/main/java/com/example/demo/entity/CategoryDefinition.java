package com.example.demo.entity;

import com.example.demo.contentHouse.api.PageItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
    private String parentId;



    public CategoryDefinition(
        String id,
        String name,
        String categoryId,
        String path,
        boolean leaf,
        int productsCount,
        String weight,
        String parentId) {

        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.path = path;
        this.leaf = leaf;
        this.productsCount = productsCount;
        this.weight = weight;
        this.parentId = parentId;
    }

    public CategoryDefinition(PageItem next) {
        if (next!=null) {
            this.setId(next.getId());
                    this.setName(next.getName());
            this.setCategoryId(next.getCategoryId());
            this.setPath(next.getPath());
            this.setLeaf(next.isLeaf());
            this.setParentId(Integer.toString(next.getProductsCount()));
            this.setWeight(next.getWeight());
            this.setParentId(next.getParentId());
        }
    }
}
