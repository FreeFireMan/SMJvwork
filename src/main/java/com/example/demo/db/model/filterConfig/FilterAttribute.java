package com.example.demo.db.model.filterConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import java.util.*;
import static com.example.demo.utils.OptionalUtils.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterAttribute {

    private int id;

    private String name;

    private Optional<String> quantifier;

    public FilterAttribute(int id, String name, Optional<String> quantifier) {
        this.id = id;
        this.name = name;
        this.quantifier = quantifier;
    }

    private Map<Integer, FilterAttributeValue> values;

    public void merge(ObjectNode node) {
        optInt(node, "id").ifPresent(id -> {
            optStr(node, "value").ifPresent(value -> {
                if (values == null) values = new HashMap<>();

                values.put(id, new FilterAttributeValue(id, value, optStr(node, "quantifier")));
            });
        });
    }
}
