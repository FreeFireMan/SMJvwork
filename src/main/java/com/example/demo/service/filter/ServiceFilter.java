package com.example.demo.service.filter;

import com.example.demo.db.model.Filter;
import com.example.demo.db.model.FilterAttrib;
import com.example.demo.db.model.filterConfig.FilterAttribute;
import com.example.demo.db.model.filterConfig.FilterAttributeValue;
import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.db.model.filterConfig.FilterGroup;
import com.example.demo.utils.OptionalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

import static com.example.demo.db.CollectionsConfig.*;

@Service
public class ServiceFilter {
    @Autowired
    private MongoTemplate mongoTemplate;

    public  ObjectNode  get(String id) {
        ObjectNode jsonNode = mongoTemplate.findOne(new Query(),ObjectNode.class,COLL_FILTER);
        ObjectNode cat = (ObjectNode) jsonNode.get("groups").get(id);
        return cat;
    }

    public ObjectNode test(ObjectNode node) {
        System.out.println(node);
        return node;
    }
}



