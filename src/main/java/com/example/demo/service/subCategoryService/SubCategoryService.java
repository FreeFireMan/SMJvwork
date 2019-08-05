package com.example.demo.service.subCategoryService;

import com.example.demo.entity.SubCategory;
import com.example.demo.contentHouse.api.PageItems;

import java.util.List;

public interface SubCategoryService {
    void save(SubCategory category);
    List<SubCategory> findAll();
    void save(List<SubCategory> category);

    void save(PageItems items);
}
