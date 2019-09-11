package com.example.demo.service.image;

import com.example.demo.utils.RequestResponseLoggingInterceptor;
import lombok.Data;
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
import java.net.URL;
import java.util.Collections;
@Service
@Data
public class ImageService {


    private static final String PATH = "C:/";
    private static final String IMAGE_JPG = ".jpg";

    private static RestTemplate makeRestTemplate() {
        RestTemplate t = new RestTemplate();
        t.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));
        return t;
    }
    /*private RestTemplate restTemplate = makeRestTemplate();*/
     private RestTemplate restTemplate = new RestTemplate();

    public  void  saveImageInServer(String url, int scaledWidth, int scaledHeight, String subPath){

       // byte[] image = restTemplate.getForObject(url, byte[].class);

        BufferedImage image =null;
        try {
            System.out.println("try ImageIO.read from");
            System.out.println(url);
            image = ImageIO.read(new URL(url));
        } catch (IOException e) {}

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

        System.out.println("nameSaveLocation.toString() : "+nameSaveLocation.toString() );
        try {
            if (parameters.get("extension").get(0).contains("png")) {
                ImageIO.write(scaleImage8(image, scaledWidth, scaledHeight), parameters.get("extension").get(0), new File(nameSaveLocation.toString()));
            }
            if (parameters.get("extension").get(0).contains("jpg")) {
                ImageIO.write(resizeImage(image, scaledWidth, scaledHeight), parameters.get("extension").get(0), new File(nameSaveLocation.toString()));
            }


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
    public static BufferedImage scaleImage11(BufferedImage image, Dimension size) {
        // Sharpen level. 10 is not sharpening (uses all pixels to work out average), 1 is no averaging, Dont go higher than 10.
        final int SHARPEN_LEVEL = 10;

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

        float xr =  (float)imgWidth / (float)image.getWidth();
        float rx =  (float)image.getWidth() / (float)imgWidth;
        int rxi =  ((int)rx) * SHARPEN_LEVEL / 10;
        float yr = (float)imgHeight / (float)image.getHeight();
        int sb = (int)rxi / 2;

        // Red, Green, Blue arrays
        long[] ra = new long[origWidth];
        int[] ga = new int[origWidth];
        int[] ba = new int[origWidth];

        int row = 0;
        int col = 0;
        float posy = 0;
        int colCount = 0;
        int owm1 = origWidth - 1;
        for (row = 0; row < origHeight; row++) {
            colCount++;
            posy += yr;
            float posx = 0;
            for (col = 0; col < origWidth; col++) {
                int ir = image.getRGB(col,row);
                int r = ir & 0x00FF0000;
                int g = ir & 0x0000FF00;
                int b = ir & 0x000000FF;
                int ro = 0;
                int go = 0;
                int bo = 0;
                if (row >= rxi) {
                    int or = image.getRGB(col,row-rxi);
                    ro = or & 0x00FF0000;
                    go = or & 0x0000FF00;
                    bo = or & 0x000000FF;
                }
                // Keep a running total of red, green, blue
                // Add arrays
                ra[col] += r;
                ga[col] += g;
                ba[col] += b;
                // Must subtract from running total
                // Subtract old
                ra[col] -= ro;
                ga[col] -= go;
                ba[col] -= bo;

                posx += xr;
                // Write pixel
                if ((posx > 1f || (col == owm1 && colCount < imgWidth)) && (posy > 1f)) {
                    long rt = 0;
                    int gt = 0;
                    int bt = 0;
                    // if sb is 0, not much scaling so dont do averaging
                    if (sb == 0) {
                        rt = ra[col];
                        gt = ga[col];
                        bt = ba[col];
                    } else {
                        int ct = 0;
                        for (int k = (col - sb); k < (col + sb); k++) {
                            if (k >= 0 && k < origWidth) {
                                ct++;
                                rt += ra[k];
                                gt += ga[k];
                                bt += ba[k];
                            }
                        }
                        if (ct == 0) ct = 1;
                        rt = (rt / ct / rxi) & 0x00FF0000;
                        gt = (gt / ct / rxi) & 0x0000FF00;
                        bt = (bt / ct / rxi) & 0x000000FF;
                    }

                    int rgb = (int)rt | gt | bt;

                    biPixels[count]=rgb;
                    count++;
                }
                if (posx > 1f) posx -= 1f;
            }
            // Should not be greater than 1
            if (posy > 1f) posy -= 1f;

            colCount = 0;
        }

        return bi;
    }


}
