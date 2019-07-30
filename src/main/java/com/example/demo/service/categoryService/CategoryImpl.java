package com.example.demo.service.categoryService;

import com.example.demo.dao.CategoryDAO;
import com.example.demo.entity.Category;
import com.example.demo.entity.Wrapper.PageItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class CategoryImpl implements CategoryService {
    @Autowired
    CategoryDAO categoryDAO;
    @Override
    public void save(Category category) {
        if (category!=null) categoryDAO.save(category);

    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public void save(List<Category> category) {
        if (category!=null) categoryDAO.saveAll(category);
    }

    @Override
    public void save(PageItems item) {
        if (item!=null) {
            categoryDAO.save(new Category(
                    item.getId(),
                    item.getName(),
                    item.getCategoryId(),
                    item.getPath(),
                    item.isLeaf(),
                    item.getProductsCount(),
                    item.getWeight(),
                    item.getParentId()
            ));
        }
    }
}
