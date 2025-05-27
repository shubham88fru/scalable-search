package com.learning.javaesplayground.p03;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.javaesplayground.AbstractTest;
import com.learning.javaesplayground.p03.entity.Product;
import com.learning.javaesplayground.p03.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.util.Streamable;

import java.util.List;

public class QueryMethodsTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(QueryMethodsTest.class);

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public void dataSetup() {
        List<Product> product = readResource("p03/products.json", new TypeReference<List<Product>>() {

        });

        productRepository.saveAll(product);
        Assertions.assertEquals(20, productRepository.count());
    }

    @Test
    public void findByCategory() {
        SearchHits<Product> searchHits = productRepository.findByCategory("Furniture");
        searchHits.forEach(print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }

    @Test
    public void findByCategories() {
        SearchHits<Product> searchHits = productRepository
                .findByCategoryIn(List.of("Furniture", "Beauty"));
        searchHits.forEach(print());
        Assertions.assertEquals(8, searchHits.getTotalHits());
    }

    @Test
    public void findByCategoryAndBrand() {
        SearchHits<Product> searchHits = productRepository
                .findByCategoryAndBrand("Furniture", "Ikea");
        searchHits.forEach(print());
        Assertions.assertEquals(2, searchHits.getTotalHits());
    }

    @Test
    public void findByName() {
        SearchHits<Product> searchHits = productRepository
                .findByName("table");
        searchHits.forEach(print());
        Assertions.assertEquals(2, searchHits.getTotalHits());
    }

    @Test
    public void findByPriceLessThan() {
        SearchHits<Product> searchHits = productRepository
                .findByPriceLessThan(80);
        searchHits.forEach(print());
        Assertions.assertEquals(5, searchHits.getTotalHits());
    }


    @Test
    public void findByPriceBetween() {
        SearchHits<Product> searchHits = productRepository
                .findByPriceBetween(10, 120, Sort.by("price"));
        searchHits.forEach(print());
        Assertions.assertEquals(8, searchHits.getTotalHits());
    }

    @Test
    public void findAllSortByQuantity() {
        Iterable<Product> iterable = productRepository
                .findAll(Sort.by("quantity"));
        iterable.forEach(print());
        Assertions.assertEquals(20, Streamable.of(iterable)
                .toList().size());
    }

    @Test
    public void findByCategoryWithPagination() {
        //page number starts from 0
        SearchPage<Product> searchPage = productRepository
                .findByCategory("Electronics", PageRequest.of(1, 4));

        searchPage.getSearchHits().forEach(print());
        Assertions.assertEquals(1, searchPage.getNumber());
        Assertions.assertEquals(3, searchPage.getTotalPages());
        Assertions.assertEquals(12, searchPage.getTotalElements());
    }

}
