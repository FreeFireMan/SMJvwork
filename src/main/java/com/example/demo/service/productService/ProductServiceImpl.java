package com.example.demo.service.productService;

import com.example.demo.dao.ProductDAO;
import com.example.demo.entity.ProductDefinition;
import com.example.demo.contentHouse.api.PageItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductDAO productDAO;


    @Override
    public void save(ProductDefinition product) {
        if (product!=null)productDAO.save(product);
    }

    @Override
    public List<ProductDefinition> findAll() {
        return productDAO.findAll();
    }

    @Override
    public void save(List<ProductDefinition> product) {
        if (product!=null)productDAO.saveAll(product);
    }

    @Override
    public void deleteAll() {
        productDAO.deleteAll();
    }

    @Override
    public void save(PageItem item) {
        item.toProduct().ifPresent(productDAO::save);
    }
}
