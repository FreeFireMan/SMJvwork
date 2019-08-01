package com.example.demo.service.ProductService;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Wrapper.PageItems;
import org.springframework.stereotype.Service;

import java.util.List;


import java.util.List;

public interface ProductService {
    void save(Product product);
    List<Product> findAll();
    void save(List<Product> product);


    void save(PageItems items);
}
