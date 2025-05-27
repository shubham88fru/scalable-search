package com.learning.javaesplayground.p03.repository;

import com.learning.javaesplayground.p03.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, Integer> {
    SearchHits<Product> findByCategory(String category);

    SearchHits<Product> findByCategoryIn(List<String> categories);

    SearchHits<Product> findByCategoryAndBrand(String category, String brand);

    SearchHits<Product> findByName(String name);

    SearchHits<Product> findByPriceLessThan(int price);

    SearchHits<Product> findByPriceBetween(Integer from, Integer to, Sort sort);

    SearchPage<Product> findByCategory(String category, Pageable pageable);

}
