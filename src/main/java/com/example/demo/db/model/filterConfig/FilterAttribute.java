package com.example.demo.db.model.filterConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterAttribute {
    private Integer id;
    private String name;
    private Optional<String> unit;
    private Map<Integer,FilterAttributeValue> values;
}
