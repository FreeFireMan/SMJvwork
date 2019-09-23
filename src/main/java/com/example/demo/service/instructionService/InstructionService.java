package com.example.demo.service.instructionService;

import lombok.Data;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Data
public class InstructionService {
    private static final String PATH = "C:/";
    private RestTemplate restTemplate = new RestTemplate();


    public void saveInstructionInServer(String url, String subPath) {
        byte[] inByte = null;
        try {
            inByte = restTemplate.getForObject(url,byte[].class);
        } catch (HttpStatusCodeException exception) {
            int statusCode = exception.getStatusCode().value();
            System.out.println("HttpStatusCodeException error : "+statusCode);
        } catch(RestClientException exception){
            String getMessage = exception.getMessage();
            System.out.println("HttpStatusCodeException error : "+getMessage);
        }
        InputStream fileStream = new ByteArrayInputStream(inByte);
        StringBuilder nameSaveLocation = new StringBuilder();
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();

        nameSaveLocation.append(PATH);
        if (subPath.length() > 0){
            nameSaveLocation.append(subPath);
        }


        File dir = new File(nameSaveLocation.toString());
        boolean created = dir.mkdirs();
        if(created){
            System.out.println("Folder has been created");
        }
        String name = parameters.get("name").get(0);
        String extension = "."+parameters.get("extension").get(0);
        nameSaveLocation.append(name).append(extension);
        Path path = Paths.get(nameSaveLocation.toString());

        try {
            Files.copy(fileStream,path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

