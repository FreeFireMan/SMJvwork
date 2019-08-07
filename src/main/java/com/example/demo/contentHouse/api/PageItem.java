package com.example.demo.contentHouse.api;

import com.example.demo.contentHouse.model.CategoryDefinition;
import com.example.demo.contentHouse.model.ProductDefinition;
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
    private Optional<String> name;
    private String categoryId;
    private Optional<String> path = Optional.empty();
    private boolean leaf;
    private int productsCount;
    private Optional<String> weight = Optional.empty();
    private Optional<String> parentId = Optional.empty();
    private Optional<String> lastUpdated = Optional.empty();
    private Optional<String> shortName = Optional.empty();
    private Optional<String> longName = Optional.empty();
    private Optional<String> baseImage = Optional.empty();
    private Optional<String> article = Optional.empty();
    private Optional<String> manufacturer = Optional.empty();
    private Optional<String> annotation = Optional.empty();
    private Optional<String> categoryIdStr = Optional.empty();
    private Optional<String> partNumber = Optional.empty();
    private Optional<String> brand = Optional.empty();
    private Optional<String> family = Optional.empty();
    private Optional<String> series = Optional.empty();
    private Optional<String> model = Optional.empty();
    private boolean hasImage;
    private boolean hasVideo;
    private boolean has360View;
    private boolean hasInstructions;
    private boolean hasMarketText;
    private Optional<String> model_color = Optional.empty();
    private Optional<String> model_union = Optional.empty();
    private Optional<String> ean = Optional.empty();

    public boolean isProduct() {
        return !isCategory();
    }

    public boolean isCategory() {
        return path.isPresent();
    }

    public Optional<CategoryDefinition> toCategory() {
        if (isCategory()) {
            return path.flatMap(path -> {
                return name.flatMap(name -> {
                    return weight.map(weight -> {
                        return new CategoryDefinition(
                                id,
                                name,
                                categoryId,
                                path,
                                this.isLeaf(),
                                this.getProductsCount(),
                                weight,
                                parentId);
                    });
                });
            });
        } else
            return Optional.empty();
    }

    public Optional<ProductDefinition> toProduct() {
        if (isProduct()) {
            return lastUpdated.flatMap(lastUpdated -> {
                return categoryIdStr.flatMap(categoryIdStr -> {
                    return partNumber.map(partNumber -> {
                        return new ProductDefinition(
                                id,
                                lastUpdated,
                                name,
                                shortName,
                                longName,
                                baseImage,
                                article,
                                manufacturer,
                                annotation,
                                categoryIdStr,
                                categoryId,
                                partNumber,
                                brand,
                                family,
                                series,
                                model,
                                this.isHasImage(),
                                this.isHasVideo(),
                                this.isHas360View(),
                                this.isHasInstructions(),
                                this.isHasMarketText(),
                                model_color,
                                model_union,
                                ean);
                    });
                });
            });
        } else
            return Optional.empty();
    }
}
