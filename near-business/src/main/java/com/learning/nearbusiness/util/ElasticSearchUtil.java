package com.learning.nearbusiness.util;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.LatLonGeoLocation;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.search.CompletionSuggester;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import co.elastic.clients.elasticsearch.core.search.SuggestFuzziness;
import co.elastic.clients.elasticsearch.core.search.Suggester;

import java.util.List;
import java.util.function.UnaryOperator;

public class ElasticSearchUtil {

    public static Suggester buildCompletionSuggester(String suggestName, String field,
                                                     String prefix, int limit) {

        SuggestFuzziness fuzziness = SuggestFuzziness.of(builder -> builder
                .fuzziness(Constants.Fuzzy.LEVEL)
                .prefixLength(Constants.Fuzzy.PREFIX_LENGTH));

        CompletionSuggester completionSuggester =
                CompletionSuggester.of(builder -> builder.field(field)
                .size(limit).fuzzy(fuzziness)
                .skipDuplicates(true));

        FieldSuggester fieldSuggester = FieldSuggester
                .of(builder -> builder.prefix(prefix).completion(completionSuggester));

        return Suggester.of(builder -> builder.suggesters(suggestName, fieldSuggester));
    }

    public static Query buildTermQuery(String field, String value, float boost) {
        TermQuery termQuery = TermQuery.of(builder -> builder
                .field(field)
                .value(value)
                .boost(boost)
                .caseInsensitive(true)
        );

        return Query.of(builder -> builder.term(termQuery));
    }

    public static Query buildRangeQuery(String field, UnaryOperator<NumberRangeQuery.Builder> function) {
        NumberRangeQuery numberRangeQuery = NumberRangeQuery.of(
                function::apply
        );

        RangeQuery rangeQuery = RangeQuery.of(builder -> builder.number(numberRangeQuery));
        return Query.of(builder -> builder.range(rangeQuery));
    }

    public static Query buildGeoDistanceQuery(String field, String distance, Double latitude, Double longitude) {
        LatLonGeoLocation latLonGeoLocation = LatLonGeoLocation
                .of(builder -> builder.lat(latitude).lon(longitude));

        GeoLocation geoLocation = GeoLocation
                .of(builder -> builder.latlon(latLonGeoLocation));

        GeoDistanceQuery geoDistanceQuery = GeoDistanceQuery
                .of(builder -> builder.field(field)
                .distance(distance)
                .location(geoLocation)
                );

        return Query.of(builder -> builder.geoDistance(geoDistanceQuery));
    }

    public static Query buildMultiMatchQuery(List<String> fields, String searchTerms) {
        MultiMatchQuery multiMatchQuery = MultiMatchQuery.of(
                builder -> builder.query(searchTerms)
                        .fields(fields)
                        .fuzziness(Constants.Fuzzy.LEVEL)
                        .prefixLength(Constants.Fuzzy.PREFIX_LENGTH)
                        .type(TextQueryType.MostFields)
                        .operator(Operator.And)
        );

        return Query.of(builder -> builder.multiMatch(multiMatchQuery));
    }

    public static Aggregation buildTermsAggregation(String field) {
        TermsAggregation termsAggregation = TermsAggregation.of(
                builder -> builder.field(field).size(10)
        );
        return Aggregation.of(builder -> builder.terms(termsAggregation));
    }
}
