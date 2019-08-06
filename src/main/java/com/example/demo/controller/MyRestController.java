package com.example.demo.controller;

import com.example.demo.entity.CategoryDefinition;
import com.example.demo.entity.ProductDefinition;
import com.example.demo.service.fetch.FetchService;
import com.example.demo.service.productService.ProductServiceImpl;
import com.example.demo.service.categoryService.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController

@RequestMapping("api")
public class  MyRestController {


    @Autowired
    private  FetchService fetchService;
    @Autowired
    private CategoryServiceImpl categoryService;
    @Autowired
    private ProductServiceImpl productService;



    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/tree")
    public List<CategoryDefinition> hello(){
       // System.out.println(categoryService.findAll());
        return categoryService.findAll();
    }
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/prod")
    public List<ProductDefinition> products(){

        return productService.findAll();
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
