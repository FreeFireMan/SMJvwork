package com.example.demo.controller;

import com.example.demo.dao.CategoryTreeDAO;
import com.example.demo.entity.Wrapper.JsonWrapper;
import com.example.demo.entity.Wrapper.PageItems;
import com.example.demo.service.ContentHouseApiClient;
import com.example.demo.service.ProductService.ProductService;
import com.example.demo.service.SubCategoryService.SubCategoryService;
import com.example.demo.service.categoryService.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    ContentHouseApiClient houseApiClient;

 /*   private static String url_1 = "http://content-house.pro/cs/api/export/categories/70037?login=lego&password=e7ddaob3&format=json";
    private static String url_2 = "http://content-house.pro/cs/api/export/categories/70037/children?login=lego&password=e7ddaob3&format=json";
    private static String url_3 = "http://content-house.pro/cs/api/export/categories/70038/products?login=lego&password=e7ddaob3&format=json";

    @Autowired
    CategoryService categoryService;



    RestTemplate restTemplate = new RestTemplate(); */

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("message","Hello world!!");
       // model.addAttribute("category", categoryService.findAll());
        houseApiClient.GetData();


        return "index";
    }

/*    @GetMapping("/getjson")
    public  String getJson() throws Exception {
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


        return "redirect:/";
    } */
}
