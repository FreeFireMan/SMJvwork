package com.example.demo.service.catalog;

import com.example.demo.db.model.CategoryHolder;
import com.example.demo.db.model.CategoryNode;
import com.example.demo.db.model.LongProductHolder;
import com.example.demo.db.model.ShortProductHolder;
import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.service.fetch.FetchService;
import com.example.demo.service.image.ImageService;
import com.example.demo.utils.OptionalUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import static com.example.demo.db.CollectionsConfig.*;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CatalogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private ImageService imageService;

    private String imageStore = "http://localhost:8080/";

    public void fetchAndUpdate() {
        final List<ObjectNode> categories = new ArrayList<>();
        final List<ObjectNode> shortProds = new ArrayList<>();
        final List<ObjectNode> longProds = new ArrayList<>();
        final FilterConfig filterConfig = new FilterConfig();

        final Optional<CategoryNode> catalog = fetchService.fetchCatalog(70037, n -> {
            if (n instanceof CategoryHolder) {
                categories.add(n.getValue());
            } else if (n instanceof ShortProductHolder) {
                shortProds.add(n.getValue());
            } else if (n instanceof LongProductHolder) {
                longProds.add(n.getValue());
                filterConfig.merge(n.getValue());
            }
        });



        // TODO: Image service stuff should rather be done more asynchronously without blocking
        System.out.println("resize shortProds");
        for (ObjectNode n: shortProds) {
            String url = new ShortProductHolder(n).getbaseImage();
            imageService.saveImageInServer(url,750,750,"rez750/");
            new ShortProductHolder(n).setBaseImage(imageStore+imageService.getOriginalName(url,"rez750/"));
        }
        System.out.println("resize longProds");
        for (ObjectNode n: longProds) {
            String url = new LongProductHolder (n).getbaseImage();
            imageService.saveImageInServer(url,1000,1000,"rez1000/");
            new LongProductHolder (n).setBaseImage(imageStore+imageService.getOriginalName(url,"rez1000/"));
        }

        mongoTemplate.findAllAndRemove(new Query(), COLL_CATEGORIES);
        if (!categories.isEmpty())
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_CATEGORIES).insert(categories).execute();

        mongoTemplate.findAllAndRemove(new Query(), COLL_PRODUCTS_SHORT);
        if (!shortProds.isEmpty())
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_SHORT).insert(shortProds).execute();

        mongoTemplate.findAllAndRemove(new Query(), COLL_PRODUCTS_LONG);
        if (!longProds.isEmpty())
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_LONG).insert(longProds).execute();

        mongoTemplate.findAllAndRemove(new Query(), COLL_CATALOG);
        catalog.ifPresent(c -> mongoTemplate.save(c.toJson(), COLL_CATALOG));

        // TODO: store `filterConfig`

        mongoTemplate.findAllAndRemove(new Query(),COLL_FILTER);
        mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED,COLL_FILTER).insert(filterConfig).execute();
    }

    public Optional<ObjectNode> get() {
        List<ObjectNode> nodes = mongoTemplate.findAll(ObjectNode.class, COLL_CATALOG);
        return OptionalUtils.head(nodes);
    }
}
