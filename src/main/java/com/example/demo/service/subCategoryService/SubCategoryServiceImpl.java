package com.example.demo.service.subCategoryService;

import com.example.demo.dao.SubCategoryDAO;
import com.example.demo.entity.SubCategory;
import com.example.demo.contentHouse.api.PageItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional

public class SubCategoryServiceImpl implements SubCategoryService {
    @Autowired
    SubCategoryDAO subCategoryDAO;

    @Override
    public void save(SubCategory category) {
        subCategoryDAO.save(category);
    }

    @Override
    public List<SubCategory> findAll() {
        return subCategoryDAO.findAll();
    }

    @Override
    public void save(List<SubCategory> category) {
        subCategoryDAO.saveAll(category);
    }

    @Override
    public void save(PageItems item) {
        if (item!=null) {
            subCategoryDAO.save(new SubCategory(
                    item.getId(),
                    item.getName(),
                    item.getCategoryId(),
                    item.getPath(),
                    item.isLeaf(),
                    item.getProductsCount(),
                    item.getWeight(),
                    item.getParentId(),
                    item.getChildren()
            ));
        }

    }
}
