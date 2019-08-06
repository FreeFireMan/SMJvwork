package com.example.demo.contentHouse;

import com.example.demo.contentHouse.api.ContentHouseResponse;
import com.example.demo.contentHouse.api.Page;
import com.example.demo.contentHouse.api.PageItem;
import com.example.demo.entity.CategoryDefinition;
import com.example.demo.entity.ProductDefinition;
import com.example.demo.service.categoryService.CategoryService;
import com.example.demo.service.productService.ProductService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Data
public class ContentHouseApi {

    private static final String API_USER = "lego";
    private static final String API_PASSWORD = "e7ddaob3";
    private static final String API_HOST = "content-house.pro";
    private static final String API_PATH = "/cs/api/export/categories";
    private static final String API_SCHEMA = "http";

    static String uri(String id, String subPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(API_SCHEMA).append("://");
        sb.append(API_HOST);
        sb.append(API_PATH).append('/');
        sb.append(id);
        if (subPath != null) {
            sb.append('/').append(subPath);
        }
        sb.append('?').append("login=").append(API_USER);
        sb.append('&').append("password=").append(API_PASSWORD);
        sb.append('&').append("format=").append("json");
        return sb.toString();
    }

    private RestTemplate restTemplate = new RestTemplate();

    private <T> Optional<T> safe(Supplier<T> fn) {
        try {
            return Optional.of(fn.get());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) return Optional.empty();
            else throw ex;
        }
    }

    private Optional<Page> fetchPage(String uri) {
        return safe(() -> restTemplate.getForObject(uri, ContentHouseResponse.class)).map(ContentHouseResponse::getPage);
    }

    /**
     * Returns a category definition
     *
     * @param id category id
     * @return
     */
    public Optional<CategoryDefinition> fetchCategory(String id) {
        return fetchPage(uri(id, null)).flatMap(page -> {
                List<PageItem> items = page.getPageItems();
                if (items.isEmpty()) return Optional.empty(); else {
                    return Optional.of(items.iterator().next()).flatMap(PageItem::toCategory);
                }
        });
    }

    /**
     * Returns a collection of children category definitions
     *
     * @param id parent category id
     * @return
     */
    public Optional<Iterable<CategoryDefinition>> fetchCategoriesOf(String id) {
        return fetchPage(uri(id, "children")).map(page -> {
            List<PageItem> items = page.getPageItems();
            Stream<CategoryDefinition> categories = items.stream()
                    .flatMap(o -> {
                        Optional<CategoryDefinition> c = o.toCategory();
                        return c.isPresent() ? Stream.of(c.get()) : Stream.<CategoryDefinition>empty();
                    });
            return (Iterable<CategoryDefinition>) categories.iterator();
        });
    }

    /**
     * Returns a collection of product definitions belonging to specified category
     * @param id
     * @return
     */
    public Optional<Iterable<ProductDefinition>> fetchProductsOf(String id) {
        return fetchPage(uri(id, "products")).map(page -> {
            List<PageItem> items = page.getPageItems();
            Stream<ProductDefinition> products = items.stream()
                    .flatMap(o -> {
                        Optional<ProductDefinition> c = o.toProduct();
                        return c.isPresent() ? Stream.of(c.get()) : Stream.<ProductDefinition>empty();
                    });
            return (Iterable<ProductDefinition>) products.iterator();
        });
    }
}
