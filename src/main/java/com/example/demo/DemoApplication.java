package com.example.demo;

import com.example.demo.service.catalog.CatalogService;
import com.example.demo.service.product.ProductService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.MultipartConfigElement;
import java.util.Optional;

@EnableScheduling
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        ProductService ps = ctx.getBean(ProductService.class);

        JsonNodeFactory f = JsonNodeFactory.instance;
        ObjectNode query = f.objectNode();
        query.putArray("Возраст от").add("8").add("9").add("7");
        query.putArray("Content status").add("Available");
        query.putArray("Product Category").add("Standard Retail");

        Page<ObjectNode> page = ps.findShortDescriptions(0, 20, 70038, query, Sort.unsorted());
        System.out.println(page.getTotalElements());
        page.getContent().forEach(c -> System.out.println(c));

    }
}
