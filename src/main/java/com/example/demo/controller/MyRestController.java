package com.example.demo.controller;
import com.example.demo.entity.Category;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.categoryService.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MyRestController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("/api")
    public List<Category> getAllCategory(){

        return categoryService.findAll();
    }


}
