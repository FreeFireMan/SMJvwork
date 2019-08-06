package com.example.demo.contentHouse.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageItem {

    @Id
    private String id;
    private String name;
    private String categoryId;
    private String path;
    private boolean leaf;
    private int productsCount;
    private String weight;
    private String parentId;
    private String lastUpdated;
    private String shortName;
    private String longName;
    private String baseImage;
    private String article;
    private String manufacturer;
    private String annotation;
    private String categoryIdStr;
    private String partNumber;
    private String brand;
    private String family;
    private String series;
    private String model;
    private boolean hasImage;
    private boolean hasVideo;
    private boolean has360View;
    private boolean hasInstructions;
    private boolean hasMarketText;
    private String model_color;
    private String model_union;
    private String ean;
}
