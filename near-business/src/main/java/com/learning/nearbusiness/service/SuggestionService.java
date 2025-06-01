package com.learning.nearbusiness.service;

import com.learning.nearbusiness.dto.SuggestionRequestParameters;
import com.learning.nearbusiness.util.Constants;
import com.learning.nearbusiness.util.NativeQueryBuilder;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SuggestionService {
    private static final Logger log = LoggerFactory.getLogger(SuggestionService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public List<String> fetchSuggestions(SuggestionRequestParameters parameters) {
        log.info("Fetching suggestions for {}", parameters);

        NativeQuery query = NativeQueryBuilder.toSuggestQuery(parameters);
        SearchHits<Object> hits = elasticsearchOperations
                .search(query, Object.class, Constants.Index.SUGGESTIONS);

        return Optional.ofNullable(hits.getSuggest())
                .map(s -> s.getSuggestion(Constants.Suggestion.SUGGEST_NAME))
                .stream()
                .map(Suggest.Suggestion::getEntries)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry::getOptions)
                .flatMap(Collection::stream)
                .map(Suggest.Suggestion.Entry.Option::getText)
                .toList();
    }
}
