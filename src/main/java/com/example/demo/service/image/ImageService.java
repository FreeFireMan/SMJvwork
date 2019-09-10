package com.example.demo.service.image;

import com.example.demo.utils.RequestResponseLoggingInterceptor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
@Service
@Data
public class ImageService {


    private static final String PATH = "src\\main\\resources\\static\\";
    private static final String IMAGE_JPG = ".jpg";

    private static RestTemplate makeRestTemplate() {
        RestTemplate t = new RestTemplate();
        t.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return t;
    }
    /*private RestTemplate restTemplate = makeRestTemplate();*/
     private RestTemplate restTemplate = new RestTemplate();

    public  void  saveImageInServer(String url, int scaledWidth, int scaledHeight, String subPath){

        byte[] image = restTemplate.getForObject(url, byte[].class);

        StringBuilder name = new StringBuilder();
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();

        name.append(PATH);
        if (subPath.length() > 0){
            name.append(subPath);

        }

        File dir = new File(name.toString());
        boolean created = dir.mkdirs();
        if(created){
            System.out.println("Folder has been created");
        }
        name.append(getOriginalName(url,""));


        System.out.println("name.toString() : "+name.toString()+"." );
        try {
            ImageIO.write(resizeImage(image,scaledWidth,scaledHeight),"jpg",new File(name.toString()+"." ));
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    public String getOriginalName(String url,String subPath)
    {
        StringBuilder nameImages = new StringBuilder();
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();
        if (subPath.length() > 0){
            nameImages.append(subPath);

        }

        nameImages.append(parameters.get("name").get(0));
        nameImages.append(IMAGE_JPG);

        return String.valueOf(nameImages);
    }
    public BufferedImage resizeImage(byte[]  image,int scaledWidth, int scaledHeight){
        ByteArrayInputStream bais = new ByteArrayInputStream(image);
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedImage outputImage =  new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }


}
