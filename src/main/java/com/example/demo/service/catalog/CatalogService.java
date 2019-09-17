package com.example.demo.service.catalog;

import com.example.demo.db.model.*;
import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.service.fetch.FetchService;
import com.example.demo.service.image.ImageService;
import com.example.demo.utils.OptionalUtils;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    private String imageStore = "http://localhost:8080/";
    //private String dir = "upload//lego//"; //for lego
    private String dir = "upload/tpv/";

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


        // TODO: Image service stuff should rather be done more asynchronously without blocking
        System.out.println("resize shortProds");
        for (ObjectNode n : shortProds) {
            String url = new ShortProductHolder(n).getbaseImage();
            String subpath = getPath(url, dir + "rez750/", "productId");
            String subPathOriginal = getPath(url, dir + "original/", "productId");
            imageService.saveImageInServer(url, 750, 750, subpath);
            imageService.saveImageInServer(url, 0, 0, subPathOriginal);
            new ShortProductHolder(n).setBaseImage(imageStore + imageService.getOriginalName(url, subPathOriginal));
            new ShortProductHolder(n).setBaseImageThumbs(imageStore + imageService.getOriginalName(url, subpath));
        }
        System.out.println("resize longProds");
        for (ObjectNode n : longProds) {
            String url = new LongProductHolder(n).getbaseImage();
            String subpath = getPath(url, dir + "rez1000/", "productId");
            String subPathOriginal = getPath(url, dir + "original/", "productId");

            ArrayNode imageNode = new LongProductHolder(n).geImages();

            imageNode.forEach(im -> {
                String urlIm = new ImageHolder((ObjectNode) im).getImagePath();

                imageService.saveImageInServer(urlIm, 1000, 1000, subpath);
                imageService.saveImageInServer(urlIm, 0, 0, subPathOriginal);
                new ImageHolder((ObjectNode) im).setImage(imageStore + imageService.getOriginalName(urlIm, subPathOriginal));
                new ImageHolder((ObjectNode) im).setThumbs(imageStore + imageService.getOriginalName(urlIm, subpath));
            });
            imageService.saveImageInServer(url, 1000, 1000, subpath);
            imageService.saveImageInServer(url, 0, 0, subPathOriginal);
            new LongProductHolder(n).setBaseImage(imageStore + imageService.getOriginalName(url, subpath));
            new LongProductHolder(n).setBaseImageThumbs(imageStore + imageService.getOriginalName(url, subPathOriginal));
            new LongProductHolder(n).setImages(imageNode);
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

        System.out.println("findAndRemove");
        mongoTemplate.dropCollection(COLL_FILTER);
        // mongoTemplate.findAndRemove(new Query(),COLL_FILTER); //TODO разобраться почему java.lang.UnsupportedOperationException: Cannot set immutable property java.util.Optional.value!
        System.out.println("Save");
        mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_FILTER).insert(filterConfig).execute();
        //mongoTemplate.save(filterConfig,COLL_FILTER);
    }

    public Optional<ObjectNode> get() {
        List<ObjectNode> nodes = mongoTemplate.findAll(ObjectNode.class, COLL_CATALOG);
        return OptionalUtils.head(nodes);
    }

    public String getPath(String url, String subPath, String getParam) {
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();
        StringBuilder name = new StringBuilder();
        name.append(subPath);
        name.append(parameters.get(getParam).get(0));
        name.append("/");
        return name.toString();
    }
}
