package com.learning.javaesplayground.p03.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

@Data
@Document(indexName = "products")
@Mapping(mappingPath = "p03/index-mapping.json")
public class Product {

    @Id
    private int id;
    private String name;
    private String brand;
    private String category;
    private int price;
    private int quantity;
}
