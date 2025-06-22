package com.learning.nearbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Facet {
    private String name;
    private List<FacetItem> items;
}
