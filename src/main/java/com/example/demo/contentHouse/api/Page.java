package com.example.demo.contentHouse.api;

import lombok.Data;

import java.util.List;

@Data
public class Page {

    private List<PageItem> pageItems;
    private String totalRows;
    private String error;
    private boolean success;
}
