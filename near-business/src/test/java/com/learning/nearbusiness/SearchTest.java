package com.learning.nearbusiness;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.nearbusiness.dto.SearchResponse;
import com.learning.nearbusiness.util.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SearchTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(SearchTest.class);

    private static final String API_PATH = "/api/search?%s";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    public void setup() {
        Map<String, Object> indexSetting = this.readResource("test-data/business-index-setting.json",
                new TypeReference<Map<String, Object>>() {});

        Map<String, Object> indexMapping = this.readResource("test-data/business-index-mapping.json",
                new TypeReference<Map<String, Object>>() {});

        List<Object> businessData = this.readResource("test-data/business-data.json",
                new TypeReference<List<Object>>() {});

        IndexOperations indexOperations =
                this.elasticsearchOperations.indexOps(Constants.Index.BUSINESS);
        indexOperations.create(indexSetting, Document.from(indexMapping));

        this.elasticsearchOperations.withRefreshPolicy(RefreshPolicy.IMMEDIATE).
                save(businessData, Constants.Index.BUSINESS);
        SearchHits<Object> hits = this.elasticsearchOperations.
                search(this.elasticsearchOperations.matchAllQuery(), Object.class, Constants.Index.BUSINESS);

        assertEquals(10, hits.getTotalHits());
    }

    @ParameterizedTest
    @MethodSource("successTestData")
    public void searchSuccessTest(String parameters, int expectedCount) {
        String path = API_PATH.formatted(parameters);
        ResponseEntity<SearchResponse> response =
                this.restTemplate.getForEntity(URI.create(path), SearchResponse.class);

        assertTrue(response.getStatusCode().is2xxSuccessful());

        SearchResponse searchResponse = response.getBody();
        log.info("response: {}", searchResponse);
        assertNotNull(searchResponse);
        assertEquals(expectedCount, searchResponse.getResults().size());
    }

    @ParameterizedTest
    @MethodSource("failureTestData")
    public void suggestionsFailureTest(String parameters){
        String path = API_PATH.formatted(parameters);
        ResponseEntity<ProblemDetail> responseEntity =
                this.restTemplate.getForEntity(URI.create(path), ProblemDetail.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals("query can not be empty", responseEntity.getBody().getDetail());
    }

    private static Stream<Arguments> successTestData() {
        return Stream.of(
                Arguments.of("query=coffee", 2),   // no filters
                Arguments.of("query=coffee&rating=4.3", 1), // rating filter
                Arguments.of("query=coffee&state=Washington", 1), // state filter
                Arguments.of("query=coffee&offerings=Wi-Fi", 1), // offerings filter
                Arguments.of("query=electronics&distance=5mi&latitude=36.5179&longitude=-94.0298", 0), // distance - no results within 5 miles
                Arguments.of("query=electronics&distance=25mi&latitude=36.5179&longitude=-94.0298", 1), // distance - 1 result within 25 miles
                Arguments.of("query=electronics&distance=5mi&latitude=36.5179", 2), // longitude is missing. so distance can not be applied
                Arguments.of("query=chain&page=0&size=3", 3), // for chain, we have 5 records. when page=0&size=3, we get the first 3
                Arguments.of("query=chain&page=1&size=3", 2), // for chain, we have 5 records. when page=1&size=3, we get the remaining 2
                Arguments.of("query=markat", 1), // fuzzy
                Arguments.of("query=XYZ", 0)  // no match
        );
    }

    private static Stream<Arguments> failureTestData() {
        return Stream.of(
                Arguments.of("query="),
                Arguments.of("")
        );
    }
}
