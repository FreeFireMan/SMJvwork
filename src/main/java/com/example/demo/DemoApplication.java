package com.example.demo;

import com.example.demo.service.catalog.CatalogService;
import com.example.demo.service.catalog.ReactiveCatalogService;
import com.example.demo.service.fetch.ReactiveFetchService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableReactiveMongoRepositories
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        ctx.getBean(ReactiveCatalogService.class).fetchAndUpdate().subscribe(System.out::println, System.err::println);
    }
}
