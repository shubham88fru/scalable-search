package com.learning.javaesplayground.p04.repository;

import com.learning.javaesplayground.p04.entity.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, String> {

    @Query("""
            {
              "multi_match": {
                "query": "#{#searchTerm}",
                "fields": ["title^3", "body"],
                "operator": "and",
                "fuzziness": 1,
                "type": "best_fields",
                "tie_breaker": 0.7
              }
            }
            """)
    SearchHits<Article> search(String searchTerm);
}
