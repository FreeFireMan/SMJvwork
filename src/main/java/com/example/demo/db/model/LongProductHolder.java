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
    public ArrayNode getImages(){return (ArrayNode) value.get("images");}
    public ArrayNode getInstructions(){return (ArrayNode) value.get("instructions");}
    public void  setPathForUploadInstruction(String url){
        value.put("upload",url);
    }
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
