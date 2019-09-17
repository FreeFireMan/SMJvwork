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
        if (categoryId>0){
       // query.addCriteria(Criteria.where("breadcrumbs").in(categoryId));
            query.addCriteria(Criteria.where("breadcrumbs").regex(String.valueOf(categoryId)));
            System.out.println("breadcrumbs"+query);
        }
        // maintain filtering
        if (json != null) {
           List<Criteria> criterias = new ArrayList<>();
            Iterator<String> fields = json.fieldNames();
            while (fields.hasNext()) {
                String k = fields.next();
                Optional<Collection<String>> values = optStrArr(json, k);
                if (values.isPresent()) {
                    Collection<String> v = values.get();
                        if (!v.isEmpty()) {
                            criterias.add(
                                Criteria
                                    .where("groups.attributes")
                                    .elemMatch(
                                        Criteria.where("name")
                                            .is(k)
                                            .and("values.value").in(v)
                                             ));
                        }
                }
            };

            if (!criterias.isEmpty()){
                Criteria crit = criterias.remove(0);
                if (!criterias.isEmpty())
                    query.addCriteria(crit.andOperator(criterias.toArray(new Criteria[criterias.size() - 1])));
                else
                    query.addCriteria(crit);
            }
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
        Query query =new  Query();
                query.addCriteria(Criteria.where("id").in(ids.getContent())).with(sort);
        System.out.println("query : "+query);
        List<ObjectNode> list = mongoTemplate
                .find(query,
                        ObjectNode.class,
                        COLL_PRODUCTS_SHORT);

        return new PageImpl<>(list, PageRequest.of(page, size,sort), ids.getTotalElements());
    }

    public Page<String> findLongDescriptionIds(
            int page,
            int size,
            int categoryId,
            ObjectNode json,
            Sort sort) {

        Query paginatedQuery = buildLongProductQuery(categoryId, json, new Sort(Sort.Direction.DESC, "date"));
        Query query = addPagination(page, size, paginatedQuery);
        System.out.println("findLongDescriptionIds : "+query);
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
    public void doSaveImages(){

    }

}
