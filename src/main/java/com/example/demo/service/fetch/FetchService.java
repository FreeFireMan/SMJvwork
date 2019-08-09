package com.example.demo.service.fetch;

import com.example.demo.contentHouse.ContentHouseApi;
import com.example.demo.db.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Service
public class FetchService {

    @Autowired
    private ContentHouseApi api;

    public Optional<CategoryNode> fetchCatalog(int rootCategoryId, Consumer<ModelHolder> callback) {
        if (log.isDebugEnabled()) log.debug("fetching catalog for {}", rootCategoryId);
        return api.fetchCategory(rootCategoryId).map(def -> {
            callback.accept(def);
            return fetchRecursively(def, true, callback);
        });
    }

    //
    // -- private members ---
    //
    private CategoryNode fetchRecursively(CategoryHolder def, boolean withProducts, Consumer<ModelHolder> callback) {
        CategoryNode node = new CategoryNode(def);
        // populate categories
        if (!def.isLeaf()) {
            api.fetchCategoriesOf(def.getId()).ifPresent(defs -> {
                for (CategoryHolder childDef : defs) {
                    callback.accept(childDef);
                    node.append(fetchRecursively(childDef, withProducts, callback));
                }
            });
        }

        // populate short products
        if (withProducts && def.isLeaf() && def.getProductsCount() > 0) {
            api.fetchProductsOf(def.getId()).ifPresent(defs -> {
                for (ShortProductHolder childDef: defs) {
                    callback.accept(childDef);
                    node.append(new ProductNode(childDef));
                    api.fetchProduct(childDef.getId()).ifPresent(callback);
                }
            });
        }

        return node;
    }
}