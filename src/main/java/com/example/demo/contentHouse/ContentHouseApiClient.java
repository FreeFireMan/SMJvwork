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

    private static String url_1 = "http://content-house.pro/cs/api/export/categories/70037?login=lego&password=e7ddaob3&format=json";
    private static String url_2 = "http://content-house.pro/cs/api/export/categories/70037/children?login=lego&password=e7ddaob3&format=json";
    private static String url_3 = "http://content-house.pro/cs/api/export/categories/70038/products?login=lego&password=e7ddaob3&format=json";


    private CategoryService categoryService;
    private ProductService productService;

    public void GetData(){
        categoryService.deleteAll();
        productService.dalateAll();
        RestTemplate restTemplate = new RestTemplate();

        List<PageItem> productList = restTemplate.getForObject(url_3, ContentHouseResponse.class).getPage().getPageItems();
        List<PageItem> category = restTemplate.getForObject(url_1, ContentHouseResponse.class).getPage().getPageItems();
        List<PageItem> subCategory = restTemplate.getForObject(url_2, ContentHouseResponse.class).getPage().getPageItems();

    //    subCategoryService.save(subCategory);
//        for (PageItems items: category ) {
//
//            for (PageItems sub: subCategory ) {
//                for (PageItems prod: productList) {
//                    if (sub.getId().equals(prod.getCategoryId())){
//                        sub.setOneChildren(prod);
//                    }
//
//                }Л
//                if (items.getId().equals(sub.getParentId())){
//                    items.setOneChildren(sub);
//                }
//
//            }
//            categoryService.save(items);
//        categoryService.deleteAll();
//        productService.dalateAll();
      //  CategoryDefinition def= fetchCategory("70037").get();

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


//        for (PageItem item: subCategory) {
//            categoryService.save(item);
//
//
//        }
//        try {
//            for (PageItem item: productList) {
//
//                productService.save(item);
//
//            }
//        } catch (Exception e){
//            System.out.println("Product save error" + e);
//        }
////        }


    }

    @Autowired
    public ContentHouseApiClient(CategoryService categoryService,ProductService productService){

        this.categoryService = categoryService;
        this.productService = productService;
    }


    @Override
    public Optional<CategoryDefinition> fetchCategory(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://content-house.pro/cs/api/export/categories/"+id+"?login=lego&password=e7ddaob3&format=json";
        List<PageItem> category =
                restTemplate.getForObject(url, ContentHouseResponse.class)
                        .getPage()
                        .getPageItems();

            System.out.println("fetchCategory work");
            CategoryDefinition def = new CategoryDefinition(category.iterator().next());


        return Optional.of(def);
    }

    @Override
    public Optional<Iterable<CategoryDefinition>> fetchCategoriesOf(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://content-house.pro/cs/api/export/categories/"+id+"/children?login=lego&password=e7ddaob3&format=json";
        List<PageItem> subCategory =
                restTemplate.getForObject(url, ContentHouseResponse.class)
                        .getPage()
                        .getPageItems();
        List<CategoryDefinition> def = new ArrayList<CategoryDefinition>();
        for (PageItem item: subCategory
             ) {
            def.add(new CategoryDefinition(item));
        }

        System.out.println("fetchCategoriesOf work");

        return Optional.of(def);
    }

    @Override
    public Optional<Iterable<ProductDefinition>> fetchProductsOf(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://content-house.pro/cs/api/export/categories/"+id+"/products?login=lego&password=e7ddaob3&format=json";
        List<PageItem> productList =
                restTemplate.getForObject(url, ContentHouseResponse.class)
                        .getPage()
                        .getPageItems();
        List<ProductDefinition> def = new ArrayList<ProductDefinition>();
        for (PageItem item: productList
        ) {
            def.add(new ProductDefinition(item));
        }

        System.out.println("fetchProductsOf work");




        return Optional.of(def);
    }
}
