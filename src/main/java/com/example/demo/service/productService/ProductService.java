package com.example.demo.service.productService;

import com.example.demo.entity.Product;
import com.example.demo.contentHouse.api.PageItem;

import java.util.List;

public interface ProductService {
    void save(Product product);
    List<Product> findAll();
    void save(List<Product> product);


    void save(PageItem items);
}
