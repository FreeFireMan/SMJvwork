package com.example.demo.service;

import com.example.demo.entity.Wrapper.JsonWrapper;
import com.example.demo.entity.Wrapper.PageItems;
import com.example.demo.service.categoryService.CategoryService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@Data
public class ContentHouseApiClient {

    private static String url_1 = "http://content-house.pro/cs/api/export/categories/70037?login=lego&password=e7ddaob3&format=json";
    private static String url_2 = "http://content-house.pro/cs/api/export/categories/70037/children?login=lego&password=e7ddaob3&format=json";
    private static String url_3 = "http://content-house.pro/cs/api/export/categories/70038/products?login=lego&password=e7ddaob3&format=json";


    private CategoryService categoryService;
    public void GetData(){
        RestTemplate restTemplate = new RestTemplate();

        List<PageItems> category = restTemplate.getForObject(url_1, JsonWrapper.class).getPage().getPageItems();
        List<PageItems> subCategory = restTemplate.getForObject(url_2, JsonWrapper.class).getPage().getPageItems();
        List<PageItems> productList = restTemplate.getForObject(url_3, JsonWrapper.class).getPage().getPageItems();
        System.out.println(productList);
        for (PageItems items: category ) {

            for (PageItems sub: subCategory ) {
                for (PageItems prod: productList) {
                    if (sub.getId().equals(prod.getCategoryId())){
                        sub.setOneChildren(prod);
                    }

                }
                if (items.getId().equals(sub.getParentId())){
                    items.setOneChildren(sub);
                }

            }
            categoryService.save(items);
        }
    }

    @Autowired
    public ContentHouseApiClient(CategoryService categoryService){
        this.categoryService = categoryService;
    }


}
