package com.example.demo.service.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import static com.example.demo.db.CollectionsConfig.*;


@Service
public class ProductService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<ObjectNode> findShortById(String id) {
        ObjectNode res = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(id)),
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
        return Optional.ofNullable(res);
    }

    public Optional<ObjectNode> findLongById(String id) {
        ObjectNode res = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(id)),
                ObjectNode.class,
                COLL_PRODUCTS_LONG);
        return Optional.ofNullable(res);
    }

    public Iterator<ObjectNode> findByCategoryId(int id) {
        return mongoTemplate.stream(
                Query.query(Criteria.where("categoryId").is(id)),
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
    }
}
