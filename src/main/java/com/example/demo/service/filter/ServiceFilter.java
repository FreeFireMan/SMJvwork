package com.example.demo.service.filter;

import com.example.demo.db.model.Filter;
import com.example.demo.db.model.FilterAttrib;
import com.example.demo.db.model.filterConfig.FilterAttribute;
import com.example.demo.db.model.filterConfig.FilterAttributeValue;
import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.db.model.filterConfig.FilterGroup;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.db.CollectionsConfig.COLL_PRODUCTS_LONG;

@Service
public class ServiceFilter {
    @Autowired
    private MongoTemplate mongoTemplate;
    final FilterConfig filterConfig = new FilterConfig();
    FilterGroup filterGroup = new FilterGroup();
    Map<String, FilterAttribute> mylist = new HashMap<>();

    public Filter getFilter() {
        Filter filter = new Filter();

        return filter;
    }


    public  FilterConfig  get() {
        List<ObjectNode> nodes = mongoTemplate.findAll(ObjectNode.class, COLL_PRODUCTS_LONG);
        for (ObjectNode g: nodes
             ) {
            filterConfig.merge(g);
        }
        filterConfig.getGroups().forEach((key,value)-> {
            value.getAttributes().forEach((k,v)->{
                mylist.put(v.getName(), (FilterAttribute) v.getValues());

            });
        });
        mylist.forEach((k,v)->{
            System.out.println("KEY "+k+" : "+v);
        });




        return filterConfig;
    }
}



