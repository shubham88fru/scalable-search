package com.learning.nearbusiness.dto;

import com.learning.nearbusiness.exceptions.BadRequestException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Data
public class SuggestionRequestParameters {
    private String prefix;
    private int limit;

    public SuggestionRequestParameters(String prefix, Integer limit) {
        this.limit = Objects.requireNonNullElse(limit, 10);
        if (!StringUtils.hasText(prefix)) {
            throw new BadRequestException("Prefix can't be empty");
        }
        this.prefix = prefix;
    }
}
