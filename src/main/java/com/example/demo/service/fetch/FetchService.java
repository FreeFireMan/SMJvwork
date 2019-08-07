package com.example.demo.service.fetch;

import com.example.demo.contentHouse.ContentHouseApi;
import com.example.demo.contentHouse.model.CategoryDefinition;
import com.example.demo.db.model.CategoryNode;
import com.example.demo.contentHouse.model.ProductDefinition;
import com.example.demo.db.model.ProductNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FetchService {

    @Autowired
    private ContentHouseApi api;

    public Optional<CategoryNode> fetchCatalog(String rootCategoryId) {
        if (log.isDebugEnabled()) log.debug("fetching catalog for {}", rootCategoryId);
        return api.fetchCategory(rootCategoryId).map(def -> {
            return fetchRecursively(def, true);
        });
    }

    public Optional<CategoryNode> fetchCategoriesIn(String rootCategoryId) {
        if (log.isDebugEnabled()) log.debug("fetching categories in {}", rootCategoryId);
        return api.fetchCategory(rootCategoryId).map(def -> {
            return fetchRecursively(def, false);
        });
    }

    public Optional<List<ProductDefinition>> fetchProductsIn(String categoryId) {
        if (log.isDebugEnabled()) log.debug("fetching products in {}", categoryId);
        return api.fetchProductsOf(categoryId);
    }

    //
    // -- private members ---
    //
    private CategoryNode fetchRecursively(CategoryDefinition def, boolean withProducts) {
        CategoryNode node = def.toNode();
        // populate categories
        if (!def.isLeaf()) {
            api.fetchCategoriesOf(def.getId()).ifPresent(defs -> {
                for (CategoryDefinition childDef : defs) {
                    CategoryNode child = fetchRecursively(childDef, withProducts);
                    node.append(child);
                }
            });
        }

        // populate products
        if (withProducts && def.isLeaf() && def.getProductsCount() > 0) {
            api.fetchProductsOf(def.getId()).ifPresent(defs -> {
                for (ProductDefinition childDef: defs) {
                    ProductNode child = childDef.toNode();
                    node.append(child);
                }
            });
        }
        return node;
    }
}
