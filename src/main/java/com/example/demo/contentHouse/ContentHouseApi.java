package com.example.demo.contentHouse;

import com.example.demo.entity.CategoryDefinition;
import com.example.demo.entity.ProductDefinition;

import java.util.Optional;


public interface ContentHouseApi {

    /**
     * Returns a category definition
     *
     * @param id category id
     * @return
     */
    public Optional<CategoryDefinition> fetchCategory(String id);

    /**
     * Returns a collection of children category definitions
     *
     * @param id parent category id
     * @return
     */
    public Optional<Iterable<CategoryDefinition>> fetchCategoriesOf(String id);

    /**
     * Returns a collection of product definitions belonging to specified category
     * @param id
     * @return
     */
    public Optional<Iterable<ProductDefinition>> fetchProductsOf(String id);
}
