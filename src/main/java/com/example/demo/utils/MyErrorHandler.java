package com.example.demo.utils;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class MyErrorHandler implements ResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        System.out.println(response);
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        System.out.println(response);
        return true;
    }

}
