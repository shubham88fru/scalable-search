package com.learning.nearbusiness.controller;

import com.learning.nearbusiness.dto.SearchRequestParameters;
import com.learning.nearbusiness.dto.SearchResponse;
import com.learning.nearbusiness.dto.SuggestionRequestParameters;
import com.learning.nearbusiness.service.SearchService;
import com.learning.nearbusiness.service.SuggestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
public class NearBusinessController {

    private final SearchService searchService;
    private final SuggestionService suggestionService;

    @GetMapping("/api/suggestions")
    public List<String> suggest(SuggestionRequestParameters parameters) {
        return suggestionService.fetchSuggestions(parameters);
    }

    @GetMapping("/api/search")
    public SearchResponse suggest(SearchRequestParameters parameters) {
        return searchService.search(parameters);
    }
}
