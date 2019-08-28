package com.example.demo.db.model.filterConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import java.util.*;
import static com.example.demo.utils.OptionalUtils.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterGroup {

    private int id;

    private String name;

    public FilterGroup(int id, String name) {
        this.id = id;
        this.name = name;
    }

    private Map<Integer, FilterAttribute> attributes;

    public void merge(ObjectNode node) {
        optInt(node, "id").ifPresent(id -> {
            optStr(node, "name").ifPresent(name -> {
                optNode(node, "values", JsonNode::isArray).ifPresent(values -> {
                    if (attributes == null) attributes = new HashMap<>();

                    FilterAttribute attr = attributes.getOrDefault(id, new FilterAttribute(id, name, optStr(node, "unit")));
                    values.forEach(val -> {
                        if (val.isObject()) {
                            attr.merge((ObjectNode) val);
                        }
                    });

                    attributes.put(id, attr);
                });
            });
        });
    }
}
