package com.example.demo.db.model.filterConfig;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.Optional;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterAttributeValue {

    private int id;

    private String value;

    private Optional<String> quantifier;
}
