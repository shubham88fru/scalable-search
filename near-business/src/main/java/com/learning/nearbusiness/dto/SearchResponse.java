package com.learning.nearbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse {
    private List<Business> results;
    private List<Facet> facets;
    private Pagination pagination;
    private long timeTaken;
}
