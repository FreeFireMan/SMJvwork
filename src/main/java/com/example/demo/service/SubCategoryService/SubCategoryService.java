package com.example.demo.service.SubCategoryService;

import com.example.demo.entity.SubCategory;
import com.example.demo.entity.Wrapper.PageItems;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SubCategoryService {
    void save(SubCategory category);
    List<SubCategory> findAll();
    void save(List<SubCategory> category);

    void save(PageItems items);
}
