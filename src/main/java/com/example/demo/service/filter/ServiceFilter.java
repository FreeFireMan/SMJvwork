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
        Map<String,JsonNode> groups = new HashMap<>();

        for (ObjectNode n : nodes
        ) {
            if (!groups.containsKey(n.get("categoryId").asText())) {
                for (JsonNode attrib: n.get("groups")
                     ) {
                    groups.put(n.get("categoryId").asText(),attrib.get("attributes"));
                }
            }if (groups.containsKey(n.get("categoryId").asText())){
                JsonNode jsonNode = groups.get(n.get("categoryId").asText());
                for (JsonNode s: n.get("groups")
                     ) {
                    for (JsonNode i: jsonNode
                    ) {

                        for (JsonNode k: s.get("attributes")
                        ) {
                            System.out.println("first i "+i.get("name"));
                            System.out.println("first k "+k.get("name"));
                            if (i.get("name") == k.get("name")){
                                System.out.println("i work");
                            }
                        }
                    }
                }



            }
        }

        return groups;
    }
}



