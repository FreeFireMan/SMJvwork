package com.example.demo.service.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.IntStream;

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
    public  Iterator<ObjectNode> get(int page,int size) {
        final Pageable pageableRequest = PageRequest.of(page, size);
        Query query = new Query();
        query.with(pageableRequest);

        return mongoTemplate.stream(
                query,
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
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
    public  List<ObjectNode> getFilterPage(ObjectNode node,Integer categoryId){
        Set<String> setID= new HashSet<>();
        List<ObjectNode> items = new ArrayList<>();
        System.out.println("Input data : "+node);
        Iterator<ObjectNode> prods = mongoTemplate.stream(
                Query.query(Criteria.where("categoryId").is(categoryId)),
                ObjectNode.class,
                COLL_PRODUCTS_LONG);
        while (prods.hasNext()){
            items.add(prods.next());
        }
        items.forEach(nodes -> {
            nodes.get("groups").forEach(g->{
                g.get("attributes").forEach(attr->{
                    System.out.println(attr.get("name"));
                    if(node.get(attr.get("name").asText()) != null)
                    attr.get("values").forEach(val->{
                        if (val.get("value").asText() == node.get(attr.get("name").asText()).asText(){
                            setID.add()
                        }
                    });
                });
            });
        });


        System.out.println(setID.toString());
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(setID));
        List<ObjectNode> list = mongoTemplate.find(query, ObjectNode.class, COLL_PRODUCTS_SHORT);
        long count = mongoTemplate.count(query, ObjectNode.class, COLL_PRODUCTS_SHORT);
        System.out.println("Output data : "+list);

        return items;
    }

}
