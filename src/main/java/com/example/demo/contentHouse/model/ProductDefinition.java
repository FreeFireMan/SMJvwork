package com.example.demo.contentHouse.model;

import com.example.demo.contentHouse.api.PageItem;
import com.example.demo.db.model.CategoryNode;
import com.example.demo.db.model.ProductNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Optional;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDefinition {

    @Id
    private String id;
    private String lastUpdated;
    private Optional<String> name;
    private Optional<String> shortName;
    private Optional<String> longName;
    private Optional<String> baseImage;
    private Optional<String> article;
    private Optional<String> manufacturer;
    private Optional<String> annotation;
    private String categoryIdStr;
    private String categoryId;
    private String partNumber;
    private Optional<String> brand;
    private Optional<String> family;
    private Optional<String> series;
    private Optional<String> model;
    private boolean hasImage;
    private boolean hasVideo;
    private boolean has360View;
    private boolean hasInstructions;
    private boolean hasMarketText;
    private Optional<String> model_color;
    private Optional<String> model_union;
    private Optional<String> ean;
    
    public ProductNode toNode() {
        return new ProductNode(
            id,
            lastUpdated,
            name.orElse(null),
            shortName.orElse(null),
            longName.orElse(null),
            baseImage.orElse(null),
            article.orElse(null),
            manufacturer.orElse(null),
            annotation.orElse(null),
            categoryIdStr,
            categoryId,
            partNumber,
            brand.orElse(null),
            family.orElse(null),
            series.orElse(null),
            model.orElse(null),
            hasImage,
            hasVideo,
            has360View,
            hasInstructions,
            hasMarketText,
            model_color.orElse(null),
            model_union.orElse(null),
            ean.orElse(null));
    }
}
