package com.learning.nearbusiness.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.learning.nearbusiness.dto.SearchRequestParameters;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

@AllArgsConstructor
public class QueryRule {

    private Predicate<SearchRequestParameters> predicate;
    private Function<SearchRequestParameters, Query> function;

    public static QueryRule of(Predicate<SearchRequestParameters> predicate,
                               Function<SearchRequestParameters, Query> function) {
        return new QueryRule(predicate, function);
    }

    public Optional<Query> build(SearchRequestParameters parameters) {
        return Optional.of(parameters)
                .filter(this.predicate)
                .map(this.function);
    }
}
