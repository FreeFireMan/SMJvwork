package com.example.demo.controller;


import com.example.demo.db.model.CatalogNode;
import com.example.demo.service.catalog.CatalogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("api/catalog")
public class CatalogController {

    @Autowired
    CatalogService catalogService;

    @PostMapping("/upgrade")
    @ResponseBody
    public void doFetch() {
        if (log.isInfoEnabled()) log.info("fetch request received");
        catalogService.fetchAndUpdate();
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<CatalogNode> doGet() {
        return catalogService.get()
            .map(n -> new ResponseEntity<>(n, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
