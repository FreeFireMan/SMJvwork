package com.example.demo.service.category;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import static com.example.demo.db.CollectionsConfig.*;


@Service
public class CategoryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<ObjectNode> findById(int id) {
        ObjectNode res = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(id)),
                ObjectNode.class,
                COLL_CATEGORIES);

        return Optional.ofNullable(res);
    }

    public Iterator<ObjectNode> findByParentId(int id) {
        return mongoTemplate.stream(
                Query.query(Criteria.where("parentId").is(id)),
                ObjectNode.class,
                COLL_CATEGORIES);
    }
}
