package com.learning.javaesplayground.p05.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.util.List;

@Data
@ToString
@Document(indexName = "garments")
@Mapping(mappingPath = "p05/index-mapping.json")
public class Garment {
    private String id;
    private String name;
    private int price;
    private List<String> color;
    private List<String> size;
    private String material;
    private String brand;
    private String occasion;
    private String neckStyle;

}
