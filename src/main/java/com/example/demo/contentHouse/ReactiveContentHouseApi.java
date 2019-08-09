package com.example.demo.contentHouse;

import com.example.demo.contentHouse.api.ContentHouseResponse;
import com.example.demo.contentHouse.api.Page;
import com.example.demo.db.model.CategoryHolder;
import com.example.demo.db.model.LongProductHolder;
import com.example.demo.db.model.ShortProductHolder;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
public class ReactiveContentHouseApi {

    private static final String API_USER = "lego";
    private static final String API_PASSWORD = "e7ddaob3";
    private static final String API_HOST = "content-house.pro";
    private static final String API_PATH = "/cs/api/export";
    private static final String API_SCHEMA = "http";

    private WebClient cli;

    public ReactiveContentHouseApi(WebClient.Builder builder) {
        this.cli = builder
            .baseUrl(API_SCHEMA + "://" + API_HOST + API_PATH)
            .build();
    }


    private <T> Mono<T> safeNotNull(Supplier<T> fn) {
        try {
            T t = fn.get();
            return t == null ? Mono.empty() : Mono.just(t);
        } catch (Throwable ex) {
            return Mono.empty();
        }
    }

    private Mono<Page> fetchPage(Function<UriBuilder, UriBuilder> uri, Object id) {
        return cli.get()
            .uri(b -> uri.apply(b)
                .queryParam("login", API_USER)
                .queryParam("password", API_PASSWORD)
                .queryParam("format", "json")
                .build(id))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onRawStatus(x -> x == 404, response -> Mono.empty())
            .bodyToMono(ContentHouseResponse.class)
            .map(ContentHouseResponse::getPage);
    }

    public Mono<CategoryHolder> fetchCategory(int id) {
        return fetchPage(b -> b.path("/categories/{id}"), id).flatMap(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items.isEmpty()) return Mono.empty(); else {
                return safeNotNull(() -> new CategoryHolder(items.iterator().next()));
            }
        });
    }

    public Mono<LongProductHolder> fetchProduct(String id) {
        return fetchPage(b -> b.path("/products/{id}"), id).flatMap(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items.isEmpty()) return Mono.empty(); else {
                return safeNotNull(() -> new LongProductHolder(items.iterator().next()));
            }
        });
    }

    public Mono<List<CategoryHolder>> fetchCategoriesOf(int id) {
        return fetchPage(b -> b.path("/categories/{id}/children"), id).map(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items == null) {
                return Collections.emptyList();
            } else {
                return items.stream().map(CategoryHolder::new).collect(Collectors.toList());
            }
        });
    }

    public Mono<List<ShortProductHolder>> fetchProductsOf(int id) {
        return fetchPage(b -> b.path("/categories/{id}/products"), id).map(page -> {
            List<ObjectNode> items = page.getPageItems();
            if (items == null) {
                return Collections.emptyList();
            } else {
                return items.stream().map(ShortProductHolder::new).collect(Collectors.toList());
            }
        });
    }
}
