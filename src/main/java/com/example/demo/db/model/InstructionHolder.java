package com.example.demo.db.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InstructionHolder implements ModelHolder {
    private ObjectNode value;
    public String getInstructionPath(){return value.get("name").asText("no_instruction");}
    public void setUploadPathInstruction(String url){
        value.put("upload",url);
    }

}
