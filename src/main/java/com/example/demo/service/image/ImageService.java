package com.example.demo.service.image;

import com.example.demo.utils.RequestResponseLoggingInterceptor;
import lombok.Data;
import org.imgscalr.Scalr;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
@Service
@Data
public class ImageService {


    private static final String PATH = "C:/";
    private static final String IMAGE_JPG = ".jpg";

     private RestTemplate restTemplate = new RestTemplate();

    public  void  saveImageInServer(String url, int scaledWidth, int scaledHeight, String subPath){
        byte[] img = null;
           img = restTemplate.getForObject(url, byte[].class);
        System.out.println("Test error");
         BufferedImage image =null;
        if(img != null) {
            InputStream in = new ByteArrayInputStream(img);
            try {
                image = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        nameSaveLocation.append(getOriginalName(url,""));

       if(scaledWidth> 0) {
           try {
               ImageIO.write(resize(image, scaledWidth, scaledHeight), parameters.get("extension").get(0), new File(nameSaveLocation.toString()));
           } catch (
                   IOException e) {
               e.printStackTrace();
           }
       }else{
           try {
               ImageIO.write(image,  parameters.get("extension").get(0), new File(nameSaveLocation.toString()));
           } catch (
                   IOException e) {
               e.printStackTrace();
           }
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
        nameImages.append(".");
        nameImages.append(parameters.get("extension").get(0));

        return String.valueOf(nameImages);
    }
    public BufferedImage resizeImage(BufferedImage  image,int scaledWidth, int scaledHeight){
       /* ByteArrayInputStream bais = new ByteArrayInputStream(image);
        BufferedImage inputImage = null;
        try {
            inputImage = ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        BufferedImage inputImage = image;

        BufferedImage outputImage =  new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }
    private static BufferedImage scaleImage8(BufferedImage source, int desiredWidth, int desiredHeight){
        double scale = (double)desiredWidth / (double)source.getWidth();
        BufferedImageOp op = new AffineTransformOp(
                AffineTransform.getScaleInstance(scale, scale),
                new RenderingHints(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC));

        return op.filter(source, null);
    }
    public static BufferedImage scaleImage2(BufferedImage image, Dimension size) {
        // If no size given, return original size
        if (size == null || size.width == 0 || size.height == 0) return image;

        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        int origWidth = image.getWidth();
        int origHeight = image.getHeight();

        float imageAspect = (float)origWidth / (float)origHeight;
        float canvasAspect = (float)width/(float)height;

        int imgWidth = width;
        int imgHeight = height;
        if (imageAspect < canvasAspect) {
            // Change width
            float w = (float)height * imageAspect;
            imgWidth = (int) w;
        } else {
            // Change height
            float h = (float)width / imageAspect;
            imgHeight = (int) h;
        }

        BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

        WritableRaster biRaster = bi.getRaster();
        int[] biPixels = ( (DataBufferInt) biRaster.getDataBuffer()).getData();

        int count = 0;

        float xr = (float)image.getWidth() / (float)imgWidth;
        float yr = (float)image.getHeight() / (float)imgHeight;
        float r = xr;
        if (yr < xr) r = yr;

        int row = 0;
        int col = 0;
        float x = 0; float y = 0;
        for (row = 0; row < imgHeight; row++) {
            x = 0;
            for (col = 0; col < imgWidth; col++) {
                int rgb = image.getRGB((int)x,(int)y);
                x += r;
                biPixels[count]=rgb;
                count++;
            }
            y += r;
        }

        return bi;
    }

    private BufferedImage resize(BufferedImage bufferedImage, Integer width, Integer height) {
        Scalr.Mode mode = (double) width / (double) height >= (double) bufferedImage.getWidth() / (double) bufferedImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH
                : Scalr.Mode.FIT_TO_HEIGHT;
        bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, mode, width, height);

        return bufferedImage;
    }


}
