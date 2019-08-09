package com.example.demo.service.fetch;

import com.example.demo.contentHouse.ContentHouseApi;
import com.example.demo.contentHouse.ReactiveContentHouseApi;
import com.example.demo.db.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReactiveFetchService {

    @Autowired
    private ReactiveContentHouseApi api;

    public Flux<CategoryNode> fetchCatalog(int rootCategoryId) {
        return api.fetchCategory(rootCategoryId)
            .flux()
            .flatMap(def -> fetchRecursively(new CategoryNode(def), true));
    }

    //
    // -- private members ---
    //
    private Flux<CategoryNode> fetchRecursively(
            CategoryNode node,
            boolean withProducts) {

        Flux<CategoryNode> res = null;

        // populate categories
        if (!node.getValue().isLeaf()) {
            res = api.fetchCategoriesOf(node.getValue().getId()).flux().flatMap(defs -> {
                Stream<Flux<CategoryNode>> children = defs
                    .stream()
                    .map(childDef -> fetchRecursively(new CategoryNode(childDef), withProducts));

                return Flux.concat(children.collect(Collectors.toList()))
                    .collectList()
                    .map(node::appendCategories);
            });
        }


        // populate short products
        if (withProducts && node.getValue().isLeaf() && node.getValue().getProductsCount() > 0) {
            Flux<CategoryNode> res2 = api.fetchProductsOf(node.getValue().getId()).flux().flatMap(defs -> {
                Stream<Mono<ProductNode>> children = defs
                    .stream()
                    .map(childDef -> api.fetchProduct(childDef.getId()).map(pd -> {
                        ProductNode pn = new ProductNode(childDef);
                        pn.setDescription(pd);
                        return pn;
                    }));

                return Flux.concat(children.collect(Collectors.toList()))
                    .collectList()
                    .map(node::appendProducts);
            });

            if (res == null) res = res2; else res = res.flatMap(c -> res2.map(c2 -> c.appendProducts(c2.getProducts())));
        }

        return res == null ? Flux.just(node) : res;
    }
}