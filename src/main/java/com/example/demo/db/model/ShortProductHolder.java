package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ShortProductHolder implements ModelHolder {

    private ObjectNode value;

    public String getId() { return value.get("id").asText("-"); }
    public String getbaseImage(){return value.get("baseImage").asText("no_Image");}
    public void  setBaseImage(String url){
        value.put("baseImage",url);
    }
    public String getBreadcrumbs() { return value.get("breadcrumbs").asText(null); }
    public void setBreadcrumbs(String x){
        value.put("breadcrumbs", x);
    }


}
