package com.example.demo.entity;

import com.example.demo.contentHouse.api.PageItem;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class ProductDefinition {

    @Id
    private String id;
    private String lastUpdated;
    private String name;
    private String shortName;
    private String longName;
    private String baseImage;
    private String article;
    private String manufacturer;
    private String annotation;
    private String categoryIdStr;
    private String categoryId;
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

    public ProductDefinition(
        String id,
        String lastUpdated,
        String name,
        String shortName,
        String longName,
        String baseImage,
        String article,
        String manufacturer,
        String annotation,
        String categoryIdStr,
        String categoryId,
        String partNumber,
        String brand,
        String family,
        String series,
        String model,
        boolean hasImage,
        boolean hasVideo,
        boolean has360View,
        boolean hasInstructions,
        boolean hasMarketText,
        String model_color,
        String model_union,
        String ean) {

        this.id = id;
        this.lastUpdated = lastUpdated;
        this.name = name;
        this.shortName = shortName;
        this.longName = longName;
        this.baseImage = baseImage;
        this.article = article;
        this.manufacturer = manufacturer;
        this.annotation = annotation;
        this.categoryIdStr = categoryIdStr;
        this.categoryId = categoryId;
        this.partNumber = partNumber;
        this.brand = brand;
        this.family = family;
        this.series = series;
        this.model = model;
        this.hasImage = hasImage;
        this.hasVideo = hasVideo;
        this.has360View = has360View;
        this.hasInstructions = hasInstructions;
        this.hasMarketText = hasMarketText;
        this.model_color = model_color;
        this.model_union = model_union;
        this.ean = ean;
    }

//    public ProductDefinition(PageItem item) {
//        this.id = item.getId();
//        this.lastUpdated = item.getLastUpdated();
//        this.name = item.getName();
//        this.shortName = item.getShortName();
//        this.longName = item.getLongName();
//        this.baseImage = item.getBaseImage();
//        this.article = item.getArticle();
//        this.manufacturer = item.getManufacturer();
//        this.annotation = item.getAnnotation();
//        this.categoryIdStr = item.getCategoryIdStr();
//        this.categoryId = item.getCategoryId();
//        this.partNumber = item.getPartNumber();
//        this.brand = item.getBrand();
//        this.family = item.getFamily();
//        this.series = item.getSeries();
//        this.model = item.getModel();
//        this.hasImage = item.isHasImage();
//        this.hasVideo = item.isHasVideo();
//        this.has360View = item.isHas360View();
//        this.hasInstructions = item.isHasInstructions();
//        this.hasMarketText = item.isHasMarketText();
//        this.model_color = item.getModel_color();
//        this.model_union = item.getModel_union();
//        this.ean = item.getEan();
//    }
}
