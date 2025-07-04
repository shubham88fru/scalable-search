package com.learning.nearbusiness.dto;

import com.learning.nearbusiness.exceptions.BadRequestException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Data
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

    public SearchRequestParameters(String query, String distance, Double latitude,
                                   Double longitude, Double rating, String state,
                                   String offerings, Integer page, Integer size) {
        if (!StringUtils.hasText(query)) {
            throw new BadRequestException("query can not be empty");
        }
        this.page = Objects.requireNonNullElse(page, 0);
        this.size = Objects.requireNonNullElse(size, 10);
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.state = state;
        this.offerings = offerings;
    }
}
