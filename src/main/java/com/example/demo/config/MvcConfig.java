package com.example.demo.config;

import jdk.nashorn.internal.runtime.Property;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String prop = System.getProperty("user.home");
        System.out.println(System.getProperty("user.dir"));

        registry
                .addResourceHandler("/upload/**")
                .addResourceLocations(prop+"/upload/");
    }
}
