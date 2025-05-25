package com.learning.javaesplayground.p02.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

@Data
@ToString
@Document(indexName = "employees")
@Mapping(mappingPath = "p02/index-mapping.json")
public class Employee {

    @Id
    private int id;
    private String name;
    private int age;
}
