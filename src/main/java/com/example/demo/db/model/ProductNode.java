package com.example.demo.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.Optional;

@Data
@AllArgsConstructor
public class ProductNode implements CatalogNode {

    @Id
    private String id;
    private String lastUpdated;
    private String name; // nullable
    private String shortName; // nullable
    private String longName; // nullable
    private String baseImage; // nullable
    private String article; // nullable
    private String manufacturer; // nullable
    private String annotation; // nullable
    private String categoryIdStr;
    private String categoryId;
    private String partNumber;
    private String brand; // nullable
    private String family; // nullable
    private String series; // nullable
    private String model; // nullable
    private boolean hasImage;
    private boolean hasVideo;
    private boolean has360View;
    private boolean hasInstructions;
    private boolean hasMarketText;
    private String model_color; // nullable
    private String model_union; // nullable
    private String ean; // nullable
}
