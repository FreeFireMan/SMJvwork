package com.example.demo.service.categoryService;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Wrapper.PageItems;

import java.util.List;

public interface CategoryService {
    void save(Category category);
    List<Category> findAll();
    void save(List<Category> category);

    void save(PageItems items);
}
