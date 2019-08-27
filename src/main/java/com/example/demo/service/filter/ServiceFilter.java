package com.example.demo.service.filter;

import com.example.demo.db.model.Filter;
import com.example.demo.db.model.FilterAttrib;
import com.example.demo.db.model.filterConfig.FilterConfig;
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
    ObjectMapper mapper = new ObjectMapper();
    private Filter filter = new Filter();
    private FilterAttrib filterAttrib = new FilterAttrib();

    public Filter getFilter() {
        Filter filter = new Filter();

        return filter;
    }


    public  Map<String,JsonNode> get() {
        List<ObjectNode> nodes = mongoTemplate.findAll(ObjectNode.class, COLL_PRODUCTS_LONG);
        List<FilterConfig> nodesGroups = null;
        Map<String,JsonNode> groups = new HashMap<>();
        filter.setGroups(new HashMap<>());
        filterAttrib.setAttributes(new HashMap<>());
        for (ObjectNode n : nodes
        ) {
            if (!groups.containsKey(n.get("id").asText())) {
                ObjectNode group;
                for (JsonNode attrib: n.get("groups")
                     ) {
                    groups.put(n.get("id").asText(),attrib.get("attributes"));
                }
            }if (groups.containsKey(n.get("id").asText())){

            }
        }
        System.out.println(groups.size());
        return groups;
    }
}



