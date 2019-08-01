package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.service.categoryService.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api")
public class MyRestController {
    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping
    public List<Category> hello(){
        return categoryService.findAll();
    }

//    @GetMapping
//    public List<String> getAllCategory(){
//        ArrayList<String> subCategories = new ArrayList<>();
//        for (Category child : categoryService.findAll()) {
//            subCategories.add(String.valueOf(child.getListSubCategory()));
//        }
//
//        return subCategories;
//    }


}
