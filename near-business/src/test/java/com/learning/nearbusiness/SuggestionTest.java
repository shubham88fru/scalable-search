package com.learning.nearbusiness;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.http.ProblemDetail;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SuggestionTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(SuggestionTest.class);

    private static final String API_PATH = "/api/suggestions?%s";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    public void setup() {
        Map<String, Object> indexMapping = this.readResource("test-data/suggestion-index-mapping.json",
                new TypeReference<Map<String, Object>>() {});

        List<Object> suggestionData = this.readResource("test-data/suggestion-data.json",
                new TypeReference<List<Object>>() {});

        IndexOperations indexOperations =
                this.elasticsearchOperations.indexOps(Constants.Index.SUGGESTIONS);
        indexOperations.create(Collections.emptyMap(), Document.from(indexMapping));

        this.elasticsearchOperations.withRefreshPolicy(RefreshPolicy.IMMEDIATE).
                save(suggestionData, Constants.Index.SUGGESTIONS);
        SearchHits<Object> hits = this.elasticsearchOperations.
                search(this.elasticsearchOperations.matchAllQuery(), Object.class, Constants.Index.SUGGESTIONS);

        assertEquals(4, hits.getTotalHits());
    }

    @ParameterizedTest
    @MethodSource("successTestData")
    public void suggestionsSuccessTest(String parameters, List<String> expectedResults) {
        String path = API_PATH.formatted(parameters);
        ResponseEntity<List<String>> response = this.restTemplate.exchange(
                RequestEntity.get(URI.create(path)).build(),
                new  ParameterizedTypeReference<List<String>>() {

                }
        );

        assertTrue(response.getStatusCode().is2xxSuccessful());

        log.info("response: {}", response.getBody());
        assertEquals(expectedResults, response.getBody());
    }

    @ParameterizedTest
    @MethodSource("failureTestData")
    public void suggestionsFailureTest(String parameters){
        var path = API_PATH.formatted(parameters);
        var responseEntity = this.restTemplate.getForEntity(URI.create(path), ProblemDetail.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is4xxClientError());
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals("Prefix can't be empty", responseEntity.getBody().getDetail());
    }

    private Stream<Arguments> successTestData() {
        return Stream.of(
                Arguments.of("prefix=w", List.of("walmart")),
                Arguments.of("prefix=c", List.of("cafe", "coffee")),
                Arguments.of("prefix=c&limit=1", List.of("cafe")),
                Arguments.of("prefix=co", List.of("coffee")),
                Arguments.of("prefix=cofe", List.of("coffee")), // fuzzy - but not cafe because of prefix 2
                Arguments.of("prefix=cffee", List.of()), // fuzzy prefix length 2
                Arguments.of("prefix=12", List.of()),
                Arguments.of("prefix=x", List.of())
        );
    }

    private static Stream<Arguments> failureTestData() {
        return Stream.of(
                Arguments.of("prefix="),
                Arguments.of("")
        );
    }
}
