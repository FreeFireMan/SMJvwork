package com.example.demo.service.productService;

import com.example.demo.entity.ProductDefinition;
import com.example.demo.contentHouse.api.PageItem;

import java.util.List;

public interface ProductService {
    void save(ProductDefinition product);
    List<ProductDefinition> findAll();
    void save(List<ProductDefinition> product);
    void deleteAll();


    void save(PageItem items);
}
