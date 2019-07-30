package com.example.demo.entity.Wrapper;

import lombok.Data;

import java.util.List;

@Data
public class Page {

    private List<PageItems> pageItems;
    private String totalRows;
    private String error;
    private boolean success;
}
