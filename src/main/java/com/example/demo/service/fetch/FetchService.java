package com.example.demo.service.fetch;

import com.example.demo.contentHouse.ContentHouseApi;
import com.example.demo.db.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Stack;
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
            return fetchRecursively(new Stack<>(), def, true, callback);
        });
    }

    //
    // -- private members ---
    //
    private CategoryNode fetchRecursively(Stack<Integer> stack, CategoryHolder def, boolean withProducts, Consumer<ModelHolder> callback) {
        CategoryNode node = new CategoryNode(def);
        // populate categories
        if (!def.isLeaf()) {
            api.fetchCategoriesOf(def.getId()).ifPresent(defs -> {
                for (CategoryHolder childDef : defs) {
                    callback.accept(childDef);
                    stack.push(childDef.getId());
                    node.append(fetchRecursively(stack, childDef, withProducts, callback));
                    stack.pop();
                }
            });
        }

        // populate short products
        if (withProducts && def.isLeaf() && def.getProductsCount() > 0) {
            StringBuilder sb = new StringBuilder();
            stack.forEach(id -> sb.append(id).append('/'));
            String breadcrumbs = sb.toString();

            api.fetchProductsOf(def.getId()).ifPresent(defs -> {
                for (ShortProductHolder childDef: defs) {
                    callback.accept(childDef);
                    node.append(new ShortProductNode(childDef));
                    api.fetchProduct(childDef.getId()).ifPresent(callback);
                }
            });
        }

        return node;
    }
}