package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Attribute implements ModelHolder {
    private ObjectNode value;
    public String getFeild(String feild) {
        return value.get(feild).asText("no_Feild");
    }

    public void setFeild(String url,String feild) {
        value.put(feild, url);
    }

}