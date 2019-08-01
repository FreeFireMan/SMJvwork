package com.example.demo.service.categoryTreeService;

import com.example.demo.entity.Category;
import com.example.demo.entity.Wrapper.PageItems;

import java.util.List;

public interface CategoryTreeService {
    void save(Category category);
    List<Category> findAll();
    void save(List<Category> category);

    void save(PageItems items);

}
