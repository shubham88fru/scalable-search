package com.learning.nearbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FacetItem {
    private String key;
    private Long count;
}
