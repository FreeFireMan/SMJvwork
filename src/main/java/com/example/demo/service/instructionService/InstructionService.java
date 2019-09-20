package com.example.demo.service.instructionService;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Data
public class InstructionService {
    private static final String PATH = "C:/";
    private RestTemplate restTemplate = new RestTemplate();


    public void saveInstructionInServer(String url, String subPath) {
        byte[] img = null;
        try {
            img = restTemplate.getForObject(url, byte[].class);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            System.out.println("HttpStatusCodeException error : "+statusCode);
        } catch(RestClientException exception){
            String getMessage = exception.getMessage();
            System.out.println("HttpStatusCodeException error : "+getMessage);
        }
    }
}

