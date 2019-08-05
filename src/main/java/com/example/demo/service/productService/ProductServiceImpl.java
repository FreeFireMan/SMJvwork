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
    public void save(PageItem item) {
        if (item!=null){
            productDAO.save(new ProductDefinition(
                    item.getId(),
                    item.getLastUpdated(),
                    item.getName(),
                    item.getShortName(),
                    item.getLongName(),
                    item.getBaseImage(),
                    item.getArticle(),
                    item.getManufacturer(),
                    item.getAnnotation(),
                    item.getCategoryIdStr(),
                    item.getCategoryId(),
                    item.getPartNumber(),
                    item.getBrand(),
                    item.getFamily(),
                    item.getSeries(),
                    item.getModel(),
                    item.isHasImage(),
                    item.isHasVideo(),
                    item.isHas360View(),
                    item.isHasInstructions(),
                    item.isHasMarketText(),
                    item.getModel_color(),
                    item.getModel_union(),
                    item.getEan()
            ));
        }

    }
}
