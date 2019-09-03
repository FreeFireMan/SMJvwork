package com.example.demo.service.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.BSON;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.db.CollectionsConfig.*;
import static com.example.demo.utils.OptionalUtils.*;


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

    public  Iterator<ObjectNode> get(int page, int size) {
        final Pageable pageableRequest = PageRequest.of(page, size);
        Query query = new Query();
        query.with(pageableRequest);

        return mongoTemplate.stream(
                query,
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
    }

    private Query addPagination(
            int page,
            int size,
            Query query) {

        // maintain pagination
        final Pageable pageableRequest = PageRequest.of(page, size);
        query.with(pageableRequest);

        return query;
    }

    private Query buildLongProductQuery(
            int categoryId,
            ObjectNode json,
            Sort sort) {

        final Query query = new Query();

        // add a product category
        query.addCriteria(Criteria.where("categoryId").is(categoryId));

        // maintain filtering
        if (json != null) {
            Criteria crit = null;
            Iterator<String> fields = json.fieldNames();
            while (fields.hasNext()) {
                String k = fields.next();
                Optional<Collection<String>> values = optStrArr(json, k);
                if (values.isPresent()) {
                    Collection<String> v = values.get();
                        if (!v.isEmpty()) {
                            if (crit == null) crit =
                                Criteria
                                    .where("groups.attributes")
                                    .elemMatch(
                                        Criteria.where("name")
                                            .is(k)
                                            .and("values.value").in(v));
                            else crit = crit.andOperator(
                            Criteria
                                .where("groups.attributes")
                                .elemMatch(
                                    Criteria.where("name")
                                        .is(k)
                                        .and("values.value").in(v)));
                        }
                }
            };

            if (crit != null) query.addCriteria(crit);
        }
        return query.with(sort);
    }

    private Query buildLongProductQuery(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Sort sort) {

        return addPagination(page, size, buildLongProductQuery(categoryId, json, sort));
    }

        /**
         * Returns the iterator of Long Product descriptions filtered accordingly with specified criterias.
         *
         * @param page
         * @param size
         * @param categoryId
         * @param json
         * @return
         */
    public Page<ObjectNode> findLongDescriptions(
        int page,
        int size,
        int categoryId,
        ObjectNode json,
        Sort sort) {

        Query paginatedQuery = buildLongProductQuery(categoryId, json, sort);
        Query query = addPagination(page, size, paginatedQuery);

        long count = mongoTemplate.count(query, BSON.class, COLL_PRODUCTS_LONG);
        List<ObjectNode> list = mongoTemplate.find(
                query,
                ObjectNode.class,
                COLL_PRODUCTS_LONG);

        return new PageImpl<>(list, PageRequest.of(page, size), count);
    }

    public Page<ObjectNode> findShortDescriptions(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Sort sort) {

        Page<String> ids = findLongDescriptionIds(page, size, categoryId, json, sort);

        List<ObjectNode> list = mongoTemplate
                .find(Query.query(Criteria.where("id").in(ids.getContent())),
                        ObjectNode.class,
                        COLL_PRODUCTS_SHORT);

        return new PageImpl<>(list, PageRequest.of(page, size), ids.getTotalElements());
    }

    public Page<String> findLongDescriptionIds(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Sort sort) {

        Query paginatedQuery = buildLongProductQuery(categoryId, json, sort);
        Query query = addPagination(page, size, paginatedQuery);
        paginatedQuery.fields().include("id").exclude("_id");

        long count = mongoTemplate.count(query, BSON.class, COLL_PRODUCTS_LONG);
        List<String> result = mongoTemplate.find(
            paginatedQuery,
            ObjectNode.class,
            COLL_PRODUCTS_LONG)
                .stream()
                .map(rec -> rec.get("id").asText())
                .collect(Collectors.toList());

        return new PageImpl<>(result, PageRequest.of(page, size), count);
    }

//    public Page<ObjectNode> getPageCat(int page, int size, Set<Integer> categoryIds) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        Query query = new Query().with(pageable);
//        query.with(pageable);
//        if (categoryIds.size()>0) {
//            query.addCriteria(Criteria.where("categoryId").in(categoryIds));
//        }
//        query.with(new Sort(Sort.Direction.DESC, "lastUpdated"));
//        List<ObjectNode> list = mongoTemplate.find(query, ObjectNode.class, COLL_PRODUCTS_SHORT);
//        long count = mongoTemplate.count(query, ObjectNode.class, COLL_PRODUCTS_SHORT);
//
//        Page<ObjectNode> resultPage = new PageImpl<ObjectNode>(list, pageable, count);
//        return resultPage;
//    }

}
