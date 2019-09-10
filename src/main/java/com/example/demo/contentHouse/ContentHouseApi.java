package com.example.demo.contentHouse;

import com.example.demo.contentHouse.api.ContentHouseResponse;
import com.example.demo.contentHouse.api.Page;
import com.example.demo.db.model.CategoryHolder;
import com.example.demo.db.model.LongProductHolder;
import com.example.demo.db.model.ShortProductHolder;
import com.example.demo.utils.RequestResponseLoggingInterceptor;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Data
public class ContentHouseApi {


    private static final String API_USER = "lego";
    private static final String API_PASSWORD = "e7ddaob3";
    private static final String API_HOST = "content-house.pro";
    private static final String API_PATH = "/cs/api/export";
    private static final String API_SCHEMA = "http";

   /* private static final String API_USER = "tpvrussia";
    private static final String API_PASSWORD = "15eXfSp3";
    private static final String API_HOST = "syndicator.tpvrussia.ru";
    private static final String API_PATH = "/api/export";
    private static final String API_SCHEMA = "http";*/

    private static String uri(String entity, Object id, String subPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(API_SCHEMA).append("://");
        sb.append(API_HOST);
        sb.append(API_PATH).append('/');
        sb.append(entity).append('/');
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
    public Optional<CategoryHolder> fetchCategory(int id) {
        return fetchPage(uri("categories", id, null)).flatMap(page -> {
                List<ObjectNode> items = page.getPageItems();
                if (items.isEmpty()) return Optional.empty(); else {
                    return safeNotNull(() -> new CategoryHolder(items.iterator().next()));
                }
        });
    }

    /**
     * Returns a category definition
     *
     * @param id category id
     * @return
     */
    public Optional<LongProductHolder> fetchProduct(String id) {
        return fetchPage(uri("products", id, null)).flatMap(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items.isEmpty()) return Optional.empty(); else {
                return safeNotNull(() -> new LongProductHolder(items.iterator().next()));
            }
        });
    }

    /**
     * Returns a collection of children category definitions
     *
     * @param id parent category id
     * @return
     */
    public Optional<List<CategoryHolder>> fetchCategoriesOf(int id) {
        return fetchPage(uri("categories", id, "children")).map(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items == null) {
                return Collections.emptyList();
            } else {
                return items.stream().map(CategoryHolder::new).collect(Collectors.toList());
            }
        });
    }

    /**
     * Returns a collection of product definitions belonging to specified category
     * @param id
     * @return
     */
    public Optional<List<ShortProductHolder>> fetchProductsOf(int id) {
        return fetchPage(uri("categories", id, "products")).map(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items == null) {
                return Collections.emptyList();
            } else {
                return items.stream().map(ShortProductHolder::new).collect(Collectors.toList());
            }
        });
    }
}
