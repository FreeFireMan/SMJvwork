package com.example.demo.service.categoryService;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.entity.CategoryDefinition;
import com.example.demo.contentHouse.api.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDAO categoryDAO;

    @Override
    public void save(CategoryDefinition category) {
        if (category!=null) categoryDAO.save(category);
    }

    @Override
    public List<CategoryDefinition> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public void save(List<CategoryDefinition> category) {
        if (category!=null) categoryDAO.saveAll(category);
    }

    @Override
    public void deleteAll() {
        categoryDAO.deleteAll();
        System.out.println("I delete All category");
    }

    @Override
    public void save(PageItem item) {
        item.toCategory().ifPresent(categoryDAO::save);
    }
}
