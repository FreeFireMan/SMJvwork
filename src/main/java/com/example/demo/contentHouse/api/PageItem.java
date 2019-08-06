package com.example.demo.contentHouse.api;

import com.example.demo.entity.CategoryDefinition;
import com.example.demo.entity.ProductDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Optional;


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

    public Optional<CategoryDefinition> toGategory(){
        Optional<CategoryDefinition> def = Optional.of(new CategoryDefinition(
                this.getId(),
                this.getName(),
                this.getCategoryId(),
                this.getPath(),
                this.isLeaf(),
                this.getProductsCount(),
                this.getWeight(),
                this.getParentId()
        ));

        return def;
    }
    public Optional<ProductDefinition> toProduct(){
        Optional<ProductDefinition> def = Optional.of(new ProductDefinition(
                this.getId(),
                this.getLastUpdated(),
                this.getName(),
                this.getShortName(),
                this.getLongName(),
                this.getBaseImage(),
                this.getArticle(),
                this.getManufacturer(),
                this.getAnnotation(),
                this.getCategoryIdStr(),
                this.getCategoryId(),
                this.getPartNumber(),
                this.getBrand(),
                this.getFamily(),
                this.getSeries(),
                this.getModel(),
                this.isHasImage(),
                this.isHasVideo(),
                this.isHas360View(),
                this.isHasInstructions(),
                this.isHasMarketText(),
                this.getModel_color(),
                this.getModel_union(),
                this.getEan()));

        return def;
    }
}
