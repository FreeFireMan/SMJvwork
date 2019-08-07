package com.example.demo.contentHouse;

import com.example.demo.contentHouse.api.ContentHouseResponse;
import com.example.demo.contentHouse.api.Page;
import com.example.demo.contentHouse.api.PageItem;
import com.example.demo.contentHouse.model.CategoryDefinition;
import com.example.demo.contentHouse.model.ProductDefinition;
import com.example.demo.utils.OptionalUtils;
import com.example.demo.utils.RequestResponseLoggingInterceptor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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

    private static RestTemplate makeRestTemplate() {
        RestTemplate t = new RestTemplate();
        t.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return t;
    }

    private RestTemplate restTemplate = makeRestTemplate();

    private <T> Optional<T> ifFound(Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) return Optional.empty();
            else throw ex;
        }
    }

    private <T> Optional<T> safeNotNull(Supplier<T> fn) {
        try {
            return Optional.ofNullable(fn.get());
        } catch (Throwable ex) {
            return Optional.empty();
        }
    }

    private Optional<Page> fetchPage(String uri) {
        return ifFound(() -> restTemplate.getForObject(uri, ContentHouseResponse.class))
            .map(ContentHouseResponse::getPage);
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
                    return safeNotNull(() -> items.iterator().next()).flatMap(PageItem::toCategory);
                }
        });
    }

    /**
     * Returns a collection of children category definitions
     *
     * @param id parent category id
     * @return
     */
    public Optional<List<CategoryDefinition>> fetchCategoriesOf(String id) {
        return fetchPage(uri(id, "children")).map(page -> {
            List<PageItem> items = page.getPageItems();
            if (items != null) {
                Stream<CategoryDefinition> categories = items.stream().flatMap(o -> OptionalUtils.toStream(o.toCategory()));
                return categories.collect(Collectors.toList());
            } else
                return Collections.emptyList();
        });
    }

    /**
     * Returns a collection of product definitions belonging to specified category
     * @param id
     * @return
     */
    public Optional<List<ProductDefinition>> fetchProductsOf(String id) {
        return fetchPage(uri(id, "products")).map(page -> {
            List<PageItem> items = page.getPageItems();
            Stream<ProductDefinition> products = items.stream().flatMap(o -> OptionalUtils.toStream(o.toProduct()));
            return products.collect(Collectors.toList());
        });
    }
}
