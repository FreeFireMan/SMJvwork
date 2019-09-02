package com.example.demo.service.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private Query buildLongProductQuery(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Optional<Sort> sort) {

        final Query query = new Query();

        // maintain pagination
       /* final Pageable pageableRequest = PageRequest.of(page, size);
        query.with(pageableRequest);*/

        // add a product category
        query.addCriteria(Criteria.where("categoryId").is(categoryId));

        // maintain filtering
        if (json != null)
            json.fieldNames().forEachRemaining(k -> {
                Optional<Collection<String>> values = optStrArr(json, k);
                values.ifPresent(v -> {
                    if (!v.isEmpty()) {
                        query.addCriteria(
                                Criteria.where("groups.attributes")
                                        .elemMatch(
                                                Criteria.where("name")
                                                        .is(k)
                                                        .and("values.value").in(v)
                                        ));
                    }
                });
            });

        sort.ifPresent(query::with);

        return query;
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
    public Iterator<ObjectNode> findLongDescriptions(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Optional<Sort> sort) {

        return mongoTemplate.stream(
                buildLongProductQuery(page, size, categoryId, json, sort),
                ObjectNode.class,
                COLL_PRODUCTS_LONG);
    }


    public Iterator<ObjectNode> findShortDescriptions(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Optional<Sort> sort) {

        List<ObjectNode> ids = findLongDescriptionIds(page, size, categoryId, json, sort);

        return mongoTemplate.stream(
                Query.query(Criteria.where("categoryId").in(ids)),
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
    }
    public Page<ObjectNode> getPagefindShortDescriptions(int page,
                                                         int size,
                                                         int categoryId,
                                                         ObjectNode json,
                                                         Optional<Sort> sort) {
        List<ObjectNode> ids = findLongDescriptionIds(page, size, categoryId, json, sort);
        System.out.println("ids"+ids);

        Set<String> idSet = new HashSet<>();
        for (ObjectNode n: ids
             ) {
            idSet.add(n.get("id").asText());
        }

        List<ObjectNode> list = mongoTemplate.find(Query.query(Criteria.where("id").in(idSet)),ObjectNode.class,COLL_PRODUCTS_SHORT);
        long count = idSet.size();
        System.out.println("list"+list.toString());
        System.out.println("count"+count);

        Pageable pageable = PageRequest.of(page, size);

        Page<ObjectNode> resultPage = new PageImpl<ObjectNode>(list, pageable, count);
        return resultPage;
    }

    public List<ObjectNode> findLongDescriptionIds(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Optional<Sort> sort) {

        Query query = buildLongProductQuery(page, size, categoryId, json, sort);
        System.out.println("findLongDescriptionIds"+query.toString());
        query.fields().include("id").exclude("_id");


        return mongoTemplate.find(
                query,
                ObjectNode.class,
                COLL_PRODUCTS_LONG);
    }



    public Page<ObjectNode> getPageCat(int page, int size, Set<Integer> categoryIds) {

        Pageable pageable = PageRequest.of(page, size);
        Query query = new Query().with(pageable);
        query.with(pageable);
        if (categoryIds.size()>0) {
            query.addCriteria(Criteria.where("categoryId").in(categoryIds));
        }
        query.with(new Sort(Sort.Direction.DESC, "lastUpdated"));
        List<ObjectNode> list = mongoTemplate.find(query, ObjectNode.class, COLL_PRODUCTS_SHORT);
        long count = mongoTemplate.count(query, ObjectNode.class, COLL_PRODUCTS_SHORT);

        Page<ObjectNode> resultPage = new PageImpl<ObjectNode>(list, pageable, count);
        return resultPage;
    }

}
