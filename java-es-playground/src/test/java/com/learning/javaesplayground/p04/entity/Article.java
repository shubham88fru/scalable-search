package com.learning.javaesplayground.p04.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

@Data
@ToString
@Document(indexName = "articles")
@Mapping(mappingPath = "p04/index-mapping.json")
public class Article {

    @Id
    private String id;
    private String title;
    private String body;
}
