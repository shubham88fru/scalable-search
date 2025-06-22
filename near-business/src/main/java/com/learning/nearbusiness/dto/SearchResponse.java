package com.learning.nearbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponse {
    private List<Business> results;
    private Pagination pagination;
    private List<Facet> facets;
    private long timeTaken;
}
