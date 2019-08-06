package com.example.demo.contentHouse;

import com.example.demo.contentHouse.api.ContentHouseResponse;
import com.example.demo.contentHouse.api.PageItem;
import com.example.demo.entity.CategoryDefinition;
import com.example.demo.entity.ProductDefinition;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.productService.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Data
public class ContentHouseApiClient implements ContentHouseApi{


    @Scheduled(cron = "0 0 * * * *")
    public void reportCurrentDate(){
        System.out.println("Scheduler Date: "+ new Date());
    }


    private CategoryService categoryService;
    private ProductService productService;

    public void GetData(){
        //TODO Don’t change anything what is irrelevant to api interface please
        categoryService.deleteAll();
        productService.deleteAll();


        RestTemplate restTemplate = new RestTemplate();

        for ( CategoryDefinition item: fetchCategoriesOf("70037").get()
             ) {
            categoryService.save(item);
            if (item.getProductsCount()>0){
                for (ProductDefinition prod: fetchProductsOf(item.getId().toString()).get()
                     ) {

                    productService.save(prod);
                }
            }
        }


    }

    @Autowired
    public ContentHouseApiClient(CategoryService categoryService,ProductService productService){

        this.categoryService = categoryService;
        this.productService = productService;
    }


    @Override
    public Optional<CategoryDefinition> fetchCategory(String id) {
        RestTemplate restTemplate = new RestTemplate(); //TODO may we have it as a class field?
        //TODO Can we externalize uri building stuff? How do you handle 404?
        String url = "http://content-house.pro/cs/api/export/categories/"+id+"?login=lego&password=e7ddaob3&format=json";
        List<PageItem> category =
                restTemplate.getForObject(url, ContentHouseResponse.class)
                        .getPage()
                        .getPageItems();

            System.out.println("fetchCategory work");

            CategoryDefinition def = null;
            if (!category.isEmpty()){
                def = category.iterator().next().toGategory().get();
            }


        return Optional.of(def); //TODO what id def is null?
    }

    @Override
    public Optional<Iterable<CategoryDefinition>> fetchCategoriesOf(String id) {
        RestTemplate restTemplate = new RestTemplate(); //TODO may we have it as a class field?
        //TODO Can we externalize uri building stuff? How do you handle 404?
        String url = "http://content-house.pro/cs/api/export/categories/"+id+"/children?login=lego&password=e7ddaob3&format=json";
        List<PageItem> subCategory =
                restTemplate.getForObject(url, ContentHouseResponse.class)
                        .getPage()
                        .getPageItems();
        List<CategoryDefinition> def = new ArrayList<CategoryDefinition>();
        for (PageItem item: subCategory
             ) {
            def.add(item.toGategory().get());
        }

        System.out.println("fetchCategoriesOf work");

        return Optional.of(def); //TODO what id def is null?
    }

    @Override
    public Optional<Iterable<ProductDefinition>> fetchProductsOf(String id) {
        RestTemplate restTemplate = new RestTemplate(); //TODO may we have it as a class field?
        //TODO Can we externalize uri building stuff? How do you handle 404?
        String url = "http://content-house.pro/cs/api/export/categories/"+id+"/products?login=lego&password=e7ddaob3&format=json";
        List<PageItem> productList =
                restTemplate.getForObject(url, ContentHouseResponse.class)
                        .getPage()
                        .getPageItems();
        List<ProductDefinition> def = new ArrayList<ProductDefinition>();
        for (PageItem item: productList
        ) {
            def.add(item.toProduct().get());
        }

        System.out.println("fetchProductsOf work");




        return Optional.of(def); //TODO what id def is null?
    }
}
