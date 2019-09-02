package com.example.demo.controller;


import com.example.demo.db.model.filterConfig.FilterConfig;
import com.example.demo.service.catalog.CatalogService;
import com.example.demo.service.category.CategoryService;
import com.example.demo.service.filter.ServiceFilter;
import com.example.demo.service.product.ProductService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api")
public class CatalogController {

    @Scheduled(cron = "0 0 0 * * *")
    public void ScheduleddoFetchAndUpdate(){
        this.doFetchAndUpdate();
    }

    @Autowired
    CatalogService catalogService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;
    @Autowired
    ServiceFilter serviceFilter;

    @PostMapping("/catalog/renew")
    public void doFetchAndUpdate() {
        if (log.isInfoEnabled()) log.info("fetch request received");

        catalogService.fetchAndUpdate();
    }

    @GetMapping("/catalog")
    public ResponseEntity<ObjectNode> doGetCatalog() {
        return catalogService.get()
            .map(n -> new ResponseEntity<>(n, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ObjectNode> doGetCategory(@PathVariable("id") int id) {
        return categoryService.findById(id)
            .map(n -> new ResponseEntity<>(n, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/categories/{id}/categories")
    public Iterator<ObjectNode> doGetCategories(@PathVariable("id") int parentId) {
        return categoryService.findByParentId(parentId);
    }

    @GetMapping("/categories/{id}/products")
    public Iterator<ObjectNode> doGetProducts(@PathVariable("id") int categoryId) {
        return productService.findByCategoryId(categoryId);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ObjectNode>  doGetProductShort(@PathVariable("id") String id) {
        return productService.findShortById(id)
                .map(n -> new ResponseEntity<>(n, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));    }

    @GetMapping("/products/{id}/full")
    public ResponseEntity<ObjectNode>  doGetProductLong(@PathVariable("id") String id) {
        return productService.findLongById(id)
                .map(n -> new ResponseEntity<>(n, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/page")
    public Page<ObjectNode> doGetPage(@RequestParam(value="page") int page,@RequestParam(value="size") int size,
                                      @RequestParam(value="cat") Set<Integer> categoryIds

    ) {

                return productService.getPageCat(page-1,size, categoryIds );

    }
    @GetMapping("/filter/{id}")
    public  ObjectNode getFilter(@PathVariable("id") String id) {
        return serviceFilter.get(id);

    }
    @PostMapping("/test/{id}")
    public List<ObjectNode> test(@RequestBody ObjectNode node,@PathVariable("id") Integer categoryId){
        return productService.getFilterPage(node,categoryId);
    }
}
