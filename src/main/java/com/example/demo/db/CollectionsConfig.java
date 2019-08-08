package com.example.demo.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;

import javax.annotation.PostConstruct;

@Configuration
public class CollectionsConfig {

    static final public String COLL_CATEGORIES = "categories";

    static final public String COLL_PRODUCTS_SHORT = "shortProds";

    static final public String COLL_PRODUCTS_LONG = "longProds";

    static final public String COLL_CATALOG = "catalog";

    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void initIndexes() {
        mongoTemplate.indexOps(COLL_CATEGORIES).ensureIndex(new Index().on("id", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps(COLL_CATEGORIES).ensureIndex(new Index().on("parentId", Sort.Direction.ASC));

        mongoTemplate.indexOps(COLL_PRODUCTS_SHORT).ensureIndex(new Index().on("id", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps(COLL_PRODUCTS_SHORT).ensureIndex(new Index().on("categoryId", Sort.Direction.ASC));

        mongoTemplate.indexOps(COLL_PRODUCTS_LONG).ensureIndex(new Index().on("id", Sort.Direction.ASC).unique());
        mongoTemplate.indexOps(COLL_PRODUCTS_LONG).ensureIndex(new Index().on("categoryId", Sort.Direction.ASC));
    }
}
