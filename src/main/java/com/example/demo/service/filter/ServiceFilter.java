package com.example.demo.service.filter;

import com.example.demo.db.model.Filter;
import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.db.model.filterConfig.FilterGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.db.CollectionsConfig.COLL_PRODUCTS_LONG;

@Service
public class ServiceFilter {
    @Autowired
    private MongoTemplate mongoTemplate;
    ObjectMapper mapper = new ObjectMapper();

    public Filter getFilter() {
        Filter filter = new Filter();

        return filter;
    }


    public List<ObjectNode> get() {
        List<ObjectNode> nodes = mongoTemplate.findAll(ObjectNode.class, COLL_PRODUCTS_LONG);
        List<FilterConfig> nodesGroups = null;
        System.out.println(nodes.get(0).get("id"));
        System.out.println(nodes.get(0).get("groups").toString());
        for (ObjectNode n:nodes
             ) {
            String key = String.valueOf(n.get("id"));
            String item = String.valueOf(n.get("groups"));
            System.out.println("key = "+key);
            System.out.println("item = "+item);
            //nodesGroups.add(new FilterConfig(String.valueOf(n.get("id"), Arrays.stream(intArray).boxed().collect(Collectors.toList()););
        }







       /* List<FilterConfig> filterConfig = mongoTemplate.findAll(FilterConfig.class,COLL_PRODUCTS_LONG).ge;
        for (FilterConfig n: filterConfig
             ) {
            System.out.println(n.toString());
        }*/

        return nodes;


    }
}


