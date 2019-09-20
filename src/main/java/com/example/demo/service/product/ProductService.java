package com.example.demo.service.product;

import com.example.demo.db.model.ImageHolder;
import com.example.demo.db.model.LongProductHolder;
import com.example.demo.db.model.ShortProductHolder;
import com.example.demo.service.image.ImageService;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.bson.BSON;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.demo.db.CollectionsConfig.*;
import static com.example.demo.utils.OptionalUtils.*;


@Service
public class ProductService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ImageService imageService;

    private String imageStore = "http://localhost:8080/";
    //private String dir = "upload//lego//"; //for lego
    private String dir = "upload/tpv/";



    public Optional<ObjectNode> findShortById(String id) {
        ObjectNode res = mongoTemplate.findOne(
                Query.query(Criteria.where("id").is(id)),
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
        return Optional.ofNullable(res);
    }
    public Iterator<ObjectNode> findShortsById(String[] ids) {
        return mongoTemplate.stream(
                Query.query(Criteria.where("id").in(ids)),
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
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
       // System.out.println("findLongDescriptionIds : "+query);
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
    public void doSaveImagesLong(){
        Query query = new Query();
        List<ObjectNode> list = mongoTemplate.find(
                query,
                ObjectNode.class,
                COLL_PRODUCTS_LONG);
        list.forEach(n->{

            saveOneLong(n);
        }   );
        mongoTemplate.findAllAndRemove(new Query(), COLL_PRODUCTS_LONG);
        if (!list.isEmpty())
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_LONG).insert(list).execute();
    }
    public void doSaveImagesShot(){
        Query query = new Query();
        List<ObjectNode> list = mongoTemplate.find(
                query,
                ObjectNode.class,
                COLL_PRODUCTS_SHORT);
        list.forEach(n->{
            saveOneShot(n);
        }   );
        mongoTemplate.findAllAndRemove(new Query(), COLL_PRODUCTS_SHORT);
        if (!list.isEmpty())
            mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_SHORT).insert(list).execute();
    }
    public void saveOneShot(ObjectNode shotP){
        String urlShot = new ShortProductHolder(shotP).getbaseImage();
        String subPathShot = getPath(urlShot, dir + "rez750/", "productId");
        String subPathOriginalShot = getPath(urlShot, dir + "original/", "productId");
        imageService.saveImageInServer(urlShot, 750, 750, subPathShot);
        imageService.saveImageInServer(urlShot, 0, 0, subPathOriginalShot);
        new ShortProductHolder(shotP).setOriginBaseImage(imageStore + imageService.getOriginalName(urlShot, subPathOriginalShot));
        new ShortProductHolder(shotP).setBaseImageThumbs(imageStore + imageService.getOriginalName(urlShot, subPathShot));

    }
    public void saveOneLong(ObjectNode longP){
        String urlLong = new LongProductHolder(longP).getbaseImage();
        String subPathLong = getPath(urlLong, dir + "rez1000/", "productId");
        String subPathOriginalLong = getPath(urlLong, dir + "original/", "productId");
        ArrayNode imageNode = new LongProductHolder(longP).geImages();

        imageNode.forEach(im -> {
            String urlIm = new ImageHolder((ObjectNode) im).getImagePath();

            imageService.saveImageInServer(urlIm, 1000, 1000, subPathLong);
            imageService.saveImageInServer(urlIm, 0, 0, subPathOriginalLong);
            new ImageHolder((ObjectNode) im).setOriginImage(imageStore + imageService.getOriginalName(urlIm, subPathOriginalLong));
            new ImageHolder((ObjectNode) im).setThumbs(imageStore + imageService.getOriginalName(urlIm, subPathLong));
        });
        imageService.saveImageInServer(urlLong, 1000, 1000, subPathLong);
        imageService.saveImageInServer(urlLong, 0, 0, subPathOriginalLong);
        new LongProductHolder(longP).setOriginBaseImage(imageStore + imageService.getOriginalName(urlLong, subPathOriginalLong));
        new LongProductHolder(longP).setBaseImageThumbs(imageStore + imageService.getOriginalName(urlLong, subPathLong));
        new LongProductHolder(longP).setImages(imageNode);
        
    }


    public void doSaveImagesForId(String id){
        //--------save shot product----------------
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        ObjectNode shotP = mongoTemplate.findOne(query, ObjectNode.class, COLL_PRODUCTS_SHORT);
        saveOneShot(shotP);
        mongoTemplate.findAndRemove(query,ShortProductHolder.class,COLL_PRODUCTS_SHORT);
        mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_SHORT).insert(shotP).execute();

        //--------save Long product----------------
        ObjectNode longP = mongoTemplate.findOne(query, ObjectNode.class, COLL_PRODUCTS_LONG);
        saveOneLong(longP);

        mongoTemplate.findAndRemove(query,LongProductHolder.class,COLL_PRODUCTS_LONG);
        mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED, COLL_PRODUCTS_LONG).insert(longP).execute();

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
