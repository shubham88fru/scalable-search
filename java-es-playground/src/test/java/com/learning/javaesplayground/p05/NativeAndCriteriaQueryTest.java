package com.learning.javaesplayground.p05;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.javaesplayground.AbstractTest;
import com.learning.javaesplayground.p05.entity.Garment;
import com.learning.javaesplayground.p05.repository.GarmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;

import java.util.List;

public class NativeAndCriteriaQueryTest extends AbstractTest {

    @Autowired
    private GarmentRepository garmentRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @BeforeAll
    public void dataSetup() {
        List<Garment> garments = readResource("p05/garments.json", new TypeReference<>() {

        });

        garmentRepository.saveAll(garments);
        Assertions.assertEquals(20, garmentRepository.count());
    }

    @Test
    public void criteriaQuery() {
        Criteria nameIsShirt = Criteria.where("name").is("shirt");
        verify(nameIsShirt, 1);

        Criteria priceInGreaterThan100 = Criteria.where("price").greaterThan(100);
        verify(priceInGreaterThan100, 5);

        verify(nameIsShirt.or(priceInGreaterThan100), 6);

        Criteria brandIsZara = Criteria.where("brand").is("Zara");
        verify(priceInGreaterThan100.and(brandIsZara.not()), 3);

        Criteria fuzzyMatchShort = Criteria.where("name").fuzzy("short");
        verify(fuzzyMatchShort, 1);
    }


    @Test
    public void boolQuery() {

        Query occasionCasual =  Query.of(b -> b.term(
                TermQuery.of(tb -> tb.field("occasion").value("Casual"))
        ));

        Query colorBrown =  Query.of(b -> b.term(
                TermQuery.of(tb -> tb.field("color").value("Brown"))
        ));

        Query priceBelow50 =  Query.of(b -> b.range(
                RangeQuery.of(rb -> rb.number(
                        NumberRangeQuery.of(nrb -> nrb.field("price").lte(50d))
                ))
        ));

        Query query = Query.of(b -> b.bool(
                BoolQuery.of(bb -> bb.filter(occasionCasual, priceBelow50).should(colorBrown))
        ));

        NativeQuery nativeQuery = NativeQuery.builder()
                .withQuery(query)
                .build();

        SearchHits<Garment> hits = elasticsearchOperations.search(nativeQuery, Garment.class);
        Assertions.assertEquals(4, hits.getTotalHits());
    }

    private void verify(Criteria criteria, int expectedResultsCount) {
        CriteriaQuery criteriaQuery = CriteriaQuery.builder(criteria).build();
        SearchHits<Garment> hits = elasticsearchOperations.search(criteriaQuery, Garment.class);
        hits.forEach(System.out::println);
        Assertions.assertEquals(expectedResultsCount, hits.getTotalHits());
    }
}
