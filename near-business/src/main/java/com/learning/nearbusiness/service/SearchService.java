package com.learning.nearbusiness.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import com.learning.nearbusiness.dto.*;
import com.learning.nearbusiness.util.Constants;
import com.learning.nearbusiness.util.NativeQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.Aggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public SearchResponse search(SearchRequestParameters parameters) {
        log.info("Search request: {}", parameters);

        NativeQuery query = NativeQueryBuilder.toSearchQuery(parameters);
        log.info("Bool query: {}", query.getQuery());

        SearchHits<Business> hits = elasticsearchOperations
                .search(query, Business.class, Constants.Index.BUSINESS);

        return buildResponse(parameters, hits);

    }

    private SearchResponse buildResponse(SearchRequestParameters parameters, SearchHits<Business> searchHits) {
        List<Business> results = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        SearchPage<Business> searchPage = SearchHitSupport.searchPageFor(searchHits,
                PageRequest.of(parameters.getPage(), parameters.getSize()));

        Pagination pagination = new Pagination(
                searchPage.getNumber(),
                searchPage.getNumberOfElements(),
                searchPage.getTotalElements(),
                searchPage.getTotalPages()
        );

        List<Facet> facets = buildFacets((List<ElasticsearchAggregation>)
                searchHits.getAggregations().aggregations());

        return new SearchResponse(
                results,
                facets,
                pagination,
                searchHits.getExecutionDuration().toMillis()
        );

    }

    private List<Facet> buildFacets(List<ElasticsearchAggregation> aggregations) {
        Map<String, Aggregate> map = aggregations.stream()
                .map(ElasticsearchAggregation::aggregation)
                .collect(Collectors.toMap(
                        Aggregation::getName,
                        Aggregation::getAggregate
                ));

        return List.of(
                buildFacet(Constants.Business.OFFERINGS_AGGREGATE_NAME,
                        map.get(Constants.Business.OFFERINGS_AGGREGATE_NAME).sterms())
        );
    }

    private Facet buildFacet(String name, StringTermsAggregate stringTermsAggregate) {
        List<FacetItem> facetItems = stringTermsAggregate.buckets()
                .array()
                .stream()
                .map(b -> new FacetItem(b.key().stringValue(), b.docCount()))
                .toList();

        return new Facet(name, facetItems);
    }

}
