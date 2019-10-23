package com.example.demo.service.catalog;

import com.example.demo.db.model.*;
import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.service.fetch.FetchService;
import com.example.demo.service.image.ImageService;
import com.example.demo.service.product.ProductService;
import com.example.demo.utils.OptionalUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.demo.db.CollectionsConfig.*;

@Slf4j
@Service
public class CatalogService {
    @Scheduled(cron = "0 0 0 * * *")
    public void ScheduleddoFetchAndUpdate() {
        this.fetchAndUpdate();
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FetchService fetchService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ProductService productService;


    public void fetchAndUpdate() {
        final List<ObjectNode> categories = new ArrayList<>();
        final List<ObjectNode> shortProds = new ArrayList<>();
        final List<ObjectNode> longProds = new ArrayList<>();
        final FilterConfig filterConfig = new FilterConfig();
        final String[] str = {null};


        // final Optional<CategoryNode> catalog = fetchService.fetchCatalog(70037, n -> { //for lego
        final Optional<CategoryNode> catalog = fetchService.fetchCatalog(118, n -> {
            if (n instanceof CategoryHolder) {
                categories.add(n.getValue());

            } else if (n instanceof ShortProductHolder) {
                str[0] = ((ShortProductHolder) n).getBreadcrumbs().toString();
                shortProds.add(n.getValue());
            } else if (n instanceof LongProductHolder) {
                longProds.add(n.getValue().put("breadcrumbs", str[0]));
                filterConfig.merge(n.getValue());

            }

        });

        mongoTemplate.findAllAndRemove(new Query(), COLL_CATEGORIES);
        if (!categories.isEmpty())
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_CATEGORIES).insert(categories).execute();

        mongoTemplate.findAllAndRemove(new Query(), COLL_PRODUCTS_SHORT);
        if (!shortProds.isEmpty()) {
            List<ObjectNode> shortProdsUniques = checkDuplicate(shortProds);
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_SHORT).insert(shortProdsUniques).execute();
        }
        System.out.println("------------------------------------------------------------------------------------------");

        mongoTemplate.findAllAndRemove(new Query(), COLL_PRODUCTS_LONG);
        if (!longProds.isEmpty()) {
            List<ObjectNode> longProdsUniques = checkDuplicate(longProds);
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_LONG).insert(longProdsUniques).execute();
        }

        mongoTemplate.findAllAndRemove(new Query(), COLL_CATALOG);
        catalog.ifPresent(c -> mongoTemplate.save(c.toJson(), COLL_CATALOG));

        //mongoTemplate.findAndRemove (new Query(),Filter.class,COLL_FILTER);
        mongoTemplate.dropCollection (COLL_FILTER);
        mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_FILTER).insert(filterConfig).execute();


        productService.doSaveFilesForLong("instructions");
        productService.doSaveFilesForLong("certificates");
        productService.doSaveFilesForLong("html_content");
        productService.doSaveFilesForLong("images");
        productService.doSaveImagesLong();
        productService.doSaveImagesShot();


    }

    public Optional<ObjectNode> get() {
        List<ObjectNode> nodes = mongoTemplate.findAll(ObjectNode.class, COLL_CATALOG);
        return OptionalUtils.head(nodes);
    }

    public List<ObjectNode> checkDuplicate(List<ObjectNode> list) {
        Set<String> duplicates = new HashSet<>();
        Set<String> uniques = new HashSet<>();

        for (ObjectNode t : list) {
            if (!uniques.add(t.get("id").asText())) {
                duplicates.add(t.get("id").asText());
            }
        }
          /*  duplicates=shortProds.stream()
               .filter(n->!uniques.add(n.get("id").asText()))
               .collect(Collectors.toSet());*/

        System.out.println(duplicates.toString());
        duplicates.forEach(n -> {
            String categoryPath = null;
            Iterator<ObjectNode> t = list.iterator();
            while (t.hasNext()) {

                ObjectNode node = t.next();
                String id = node.get("id").asText();

                if (n.equals(id) && categoryPath != null) {
                    String temp = node.get("breadcrumbs").asText() + categoryPath;
                    node.put("breadcrumbs", temp);
                }
                if (n.equals(id) && categoryPath == null) {
                    categoryPath = node.get("breadcrumbs").asText();
                    t.remove();
                }
            }
        });


        return list;
    }

}
