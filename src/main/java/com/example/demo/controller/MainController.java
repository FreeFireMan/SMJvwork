package com.example.demo.controller;

import com.example.demo.entity.Category;
import com.example.demo.entity.Wrapper.JsonWrapper;
import com.example.demo.entity.Wrapper.PageItems;
import com.example.demo.service.categoryService.CategoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.util.List;

@Controller
public class MainController {



    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("message","Hello world!!");
        return "index";
    }
    @GetMapping("/getjson")
    public  String getJson() throws MalformedURLException {
        @Autowired
        CategoryImpl saveCategoryImpl;

        RestTemplate restTemplate = new RestTemplate();
        String url_1 = "http://content-house.pro/cs/api/export/categories/70037?login=lego&password=e7ddaob3&format=json";
        String url_2 = "http://content-house.pro/cs/api/export/categories/70037/children?login=lego&password=e7ddaob3&format=json";
        String url_3 = "http://content-house.pro/cs/api/export/categories/70038/products?login=lego&password=e7ddaob3&format=json";
        try {

            List<PageItems> category = restTemplate.getForObject(url_1, JsonWrapper.class).getPage().getPageItems();
            List<PageItems> subCategory = restTemplate.getForObject(url_2, JsonWrapper.class).getPage().getPageItems();
            List<PageItems> productList = restTemplate.getForObject(url_3, JsonWrapper.class).getPage().getPageItems();
            System.out.println(category);
            System.out.println(subCategory);
            System.out.println(productList);
            for (PageItems items: category ) {

                System.out.println(items.toString());
                saveCategoryImpl.save(items);
            }
//            for (Page color : forNow) {
//                System.out.println(color.toString());;
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        return "redirect:/";
    }

    private void printItemList(PageItems item) {
        System.out.println("test " + item);
    }

}
