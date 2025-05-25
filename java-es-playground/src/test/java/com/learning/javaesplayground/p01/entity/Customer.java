package com.learning.javaesplayground.p01.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName = "customers")
@Setting(settingPath = "p01/index-setting.json")
@Mapping(mappingPath = "p01/index-mapping.json")
public class Customer {

    @Id
    private String id;
    private String name;
    private int age;
}
