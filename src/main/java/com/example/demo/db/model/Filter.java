package com.example.demo.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {

    private int id;
    private int categoryId;
    private int minAge;
    private int maxAge;
    private int minCountPices;
    private int maxCountPices;
    private String contentStatus;
    private String productCategory;
    private String sex;
    private String dateStart;
    private String dateEnd;

}
