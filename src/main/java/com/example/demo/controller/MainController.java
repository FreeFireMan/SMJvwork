package com.example.demo.controller;


import com.example.demo.contentHouse.ContentHouseApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    @Autowired
    ContentHouseApiClient houseApiClient;


    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("message","Hello world!!");
       // model.addAttribute("category", categoryService.findAll());



        return "index";
    }


    @GetMapping("/getjson")
    public String getData(){
        houseApiClient.GetData();
        return "redirect:/";
    }

}
