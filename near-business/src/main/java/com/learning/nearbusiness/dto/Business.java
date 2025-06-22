package com.learning.nearbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.List;

@Data
@AllArgsConstructor
public class Business {
    private String id;
    private String name;
    private String description;
    private String address;
    private List<String> category;
    private List<String> offerings;
    @Field(name="avg_rating")
    private Float rating;
    @Field(name="num_of_reviews")
    private Integer reviewsCount;
    private String url;
}
