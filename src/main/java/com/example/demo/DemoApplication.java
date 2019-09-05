package com.example.demo;

import com.example.demo.service.product.ProductService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.function.Consumer;

@EnableScheduling
@SpringBootApplication
public class DemoApplication {

    private ConfigurableApplicationContext ctx;
    private ProductService ps;
    private JsonNodeFactory f = JsonNodeFactory.instance;

    public DemoApplication(ConfigurableApplicationContext ctx) {
        this.ctx = ctx;
        this.ps = ctx.getBean(ProductService.class);
    }

    private void q(Consumer<ObjectNode> q) {
        ObjectNode query = f.objectNode();
        q.accept(query);
        Page<ObjectNode> page = ps.findShortDescriptions(0, 20, 70038, query, Sort.unsorted());
        System.out.println(page.getTotalElements());
        page.getContent().forEach(c -> System.out.println(c));
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);
        DemoApplication app = new DemoApplication(ctx);

        // test filter. several expressions
        app.q(query -> {
            query.putArray("Возраст от").add("8").add("9").add("7");
            query.putArray("Content status").add("Available");
            query.putArray("Product Category").add("Standard Retail");
        });

        // test filter. no expressions
        app.q(query -> { });

        // test filter. one expression
        app.q(query -> {
            query.putArray("Возраст от").add("8");
        });
    }
}
