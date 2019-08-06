package com.example.demo.service.categoryService;

import com.example.demo.entity.CategoryDefinition;
import com.example.demo.contentHouse.api.PageItem;

import java.util.List;

public interface CategoryService {
    void save(CategoryDefinition category);
    List<CategoryDefinition> findAll();
    void save(List<CategoryDefinition> category);
    void deleteAll();

    void save(PageItem items);
}
