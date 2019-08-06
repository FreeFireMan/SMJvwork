package com.example.demo.service.fetch;

import com.example.demo.contentHouse.ContentHouseApi;
import com.example.demo.entity.CategoryDefinition;
import com.example.demo.entity.CategoryNode;
import com.example.demo.entity.ProductDefinition;
import com.example.demo.entity.ProductNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FetchService {

    @Autowired
    private ContentHouseApi api;

    public Optional<CategoryNode> fetchCatalog(String rootCategoryId) {
        return api.fetchCategory(rootCategoryId).map(def -> {
            return fetchRecursively(def, true);
        });
    }

    public Optional<CategoryNode> fetchCategoriesIn(String rootCategoryId) {
        return api.fetchCategory(rootCategoryId).map(def -> {
            return fetchRecursively(def, false);
        });
    }

    public Optional<Iterable<ProductDefinition>> fetchProductsIn(String categoryId) {
        return api.fetchProductsOf(categoryId);
    }

    //
    // -- private members ---
    //
    private CategoryNode fetchRecursively(CategoryDefinition def, boolean withProducts) {
        CategoryNode node = new CategoryNode(def);
        // populate categories
        api.fetchCategoriesOf(def.getId()).ifPresent(defs -> {
            for (CategoryDefinition childDef: defs) {
                CategoryNode child = fetchRecursively(childDef, withProducts);
                node.append(child);
            }
        });
        // populate products
        if (withProducts && def.isLeaf() && def.getProductsCount() > 0) {
            api.fetchProductsOf(def.getId()).ifPresent(defs -> {
                for (ProductDefinition childDef: defs) {
                    ProductNode child = new ProductNode(childDef);
                    node.append(child);
                }
            });
        }
        return node;
    }
}
