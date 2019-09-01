package com.example.demo.db.model.filterConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.*;
import java.util.*;
import static com.example.demo.utils.OptionalUtils.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterConfig {

    private Map<Integer, FilterGroup> groups;

    public void merge(ObjectNode node) {
        int idcat = node.get("categoryId").intValue();
        node.get("groups").forEach(g -> {
            if (g.isObject()) {
                ObjectNode gn = (ObjectNode) g;
                optInt(gn, "id").ifPresent(id -> {
                    optStr(gn, "name").ifPresent(name -> {
                        optNode(gn, "attributes", JsonNode::isArray).ifPresent(attributes -> {
                            if (groups == null) groups = new HashMap<>();

                            FilterGroup group = groups.getOrDefault(idcat, new FilterGroup(id, name));
                            attributes.forEach(attribute -> {
                                if (attribute.isObject()) {
                                   group.merge((ObjectNode) attribute);

                                }
                            });

                            groups.put(idcat, group);


                        });
                    });
                });
            }
        });
    }
}
