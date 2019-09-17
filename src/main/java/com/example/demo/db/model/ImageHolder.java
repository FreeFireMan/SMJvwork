package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageHolder implements ModelHolder  {
    private ObjectNode value;
    public String getImagePath(){return value.get("name").asText("no_Image");}


    public void setImage(String url){
        value.put("name",url);
    }
    public void setThumbs(String url){
        value.put("Thumbs",url);
    }
}
