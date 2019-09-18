package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LongProductHolder implements ModelHolder {

    private ObjectNode value;
    public String getbaseImage(){return value.get("baseImage").asText("no_Image");}
    public ArrayNode geImages(){return (ArrayNode) value.get("images");}
    public void  setOriginBaseImage(String url){
        value.put("originBaseImage",url);
    }
    public void  setImages(ArrayNode images){

        value.set("images", images);
    }
    public void setBaseImageThumbs(String url){
        value.put("baseImageThumbs",url);
    }
}
