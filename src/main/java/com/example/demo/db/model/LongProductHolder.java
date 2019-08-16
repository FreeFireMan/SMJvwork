package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LongProductHolder implements ModelHolder {

    private ObjectNode value;
    public String getbaseImage(){return value.get("baseImage").asText("no_Image");}
    public void  setBaseImage(String url){
        value.put("baseImage",url);
    }
}
