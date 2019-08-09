package com.example.demo.service.catalog;

import com.example.demo.service.fetch.ReactiveFetchService;
import com.example.demo.utils.OptionalUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static com.example.demo.db.CollectionsConfig.*;

@Slf4j
@Service
public class ReactiveCatalogService {

    @Autowired
    private ReactiveMongoTemplate m;

    @Autowired
    private ReactiveFetchService fetchService;

    public Flux<ObjectNode> fetchAndUpdate() {
        return fetchService
            .fetchCatalog(70037)
            .flatMap(c -> m.findAllAndRemove(new Query(), COLL_CATEGORIES).map(x -> c))
            .flatMap(c -> m.insertAll(Mono.just(c.flattenedCategories()), COLL_CATEGORIES).map(x -> c))
            .flatMap(c -> m.findAllAndRemove(new Query(), COLL_PRODUCTS_SHORT).map(x -> c))
            .flatMap(c -> m.insertAll(Mono.just(c.flattenedProductShorts()), COLL_PRODUCTS_SHORT).map(x -> c))
            .flatMap(c -> m.findAllAndRemove(new Query(), COLL_PRODUCTS_LONG).map(x -> c))
            .flatMap(c -> m.insertAll(Mono.just(c.flattenedProductLongs()), COLL_PRODUCTS_LONG).map(x -> c))
            .flatMap(c -> m.findAllAndRemove(new Query(), COLL_CATALOG).map(x -> c))
            .flatMap(c -> m.save(c.toJson()));
    }

    public Mono<ObjectNode> get() {
        Flux<ObjectNode> nodes = m.findAll(ObjectNode.class, COLL_CATALOG);
        return nodes.publishNext();
    }
}
