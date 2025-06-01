package com.learning.nearbusiness.util;

import co.elastic.clients.elasticsearch.core.search.Suggester;
import com.learning.nearbusiness.dto.SuggestionRequestParameters;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;

public class NativeQueryBuilder {

    public static NativeQuery toSuggestQuery(SuggestionRequestParameters parameters) {
        Suggester suggester = ElasticSearchUtil.buildCompletionSuggester(
                Constants.Suggestion.SUGGEST_NAME,
                Constants.Suggestion.SEARCH_TERM,
                parameters.getPrefix(),
                parameters.getLimit()
        );

        return NativeQuery.builder()
                .withSuggester(suggester)
                .withMaxResults(0)
                .withSourceFilter(FetchSourceFilter
                        .of(b -> b.withExcludes("*"))) //only suggestion
                .build();
    }
}
