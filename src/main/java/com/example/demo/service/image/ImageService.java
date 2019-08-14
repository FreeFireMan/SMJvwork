package com.example.demo.service.image;

import com.example.demo.utils.RequestResponseLoggingInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
@Service
public class ImageService {


    private static final String PATH = "src\\main\\resources\\static\\";
    private static final String IMAGE_JPG = ".jpg";

    private static RestTemplate makeRestTemplate() {
        RestTemplate t = new RestTemplate();
        t.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return t;
    }
    private RestTemplate restTemplate = makeRestTemplate();

    public  void  saveImageInServer(String url){

        byte[] image = restTemplate.getForObject(url, byte[].class);
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();
        StringBuilder name = new StringBuilder();
        name.append(PATH);
        name.append(parameters.get("name").get(0));
        name.append(IMAGE_JPG);
        try {
            Files.write(Paths.get(String.valueOf(name)), image);
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

}
