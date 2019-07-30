package com.example.demo.entity.Wrapper;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class PageItems {
    @Id
    private String id;
    private String name;
    private String categoryId;
    private String path;
    private Object[] children;
    private boolean leaf;
    private String productsCount;
    private String weight;
    private String parentId;


}
