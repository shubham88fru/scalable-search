package com.learning.javaesplayground.p01.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Document(indexName = "reviews")
@Setting(shards = 2, replicas = 2)
public class Review {

    @Id
    private String id;
}
