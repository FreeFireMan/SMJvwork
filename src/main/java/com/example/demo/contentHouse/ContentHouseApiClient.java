package com.example.demo.contentHouse;

import com.example.demo.contentHouse.api.ContentHouseResponse;
import com.example.demo.contentHouse.api.PageItem;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.productService.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

@Component
@Data
public class ContentHouseApiClient {

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
        RestTemplate restTemplate = new RestTemplate();

        List<PageItem> productList = restTemplate.getForObject(url_3, ContentHouseResponse.class).getPage().getPageItems();
        List<PageItem> category = restTemplate.getForObject(url_1, ContentHouseResponse.class).getPage().getPageItems();
        List<PageItem> subCategory = restTemplate.getForObject(url_2, ContentHouseResponse.class).getPage().getPageItems();
        System.out.println(productList);
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
        for (PageItem item: subCategory) {
            categoryService.save(item);

        }
        try {
            for (PageItem item: productList) {
                System.out.println(item);
                productService.save(item);

            }
        } catch (Exception e){
            System.out.println("Product save error" + e);
        }
//        }

    }

    @Autowired
    public ContentHouseApiClient(CategoryService categoryService,ProductService productService){

        this.categoryService = categoryService;
        this.productService = productService;
    }


}
