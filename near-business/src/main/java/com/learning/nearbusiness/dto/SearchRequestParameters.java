package com.learning.nearbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRequestParameters {
    private String query;
    private String distance;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private String state;
    private String offerings;
    private Integer page;
    private Integer size;
}
