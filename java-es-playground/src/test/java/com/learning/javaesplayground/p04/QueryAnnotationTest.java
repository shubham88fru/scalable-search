package com.learning.javaesplayground.p04;

import com.fasterxml.jackson.core.type.TypeReference;
import com.learning.javaesplayground.AbstractTest;
import com.learning.javaesplayground.p04.entity.Article;
import com.learning.javaesplayground.p04.repository.ArticleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;


public class QueryAnnotationTest extends AbstractTest {

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeAll
    public void dataSetup() {
        List<Article> articles = readResource("p04/articles.json", new TypeReference<List<Article>>() {

        });

        articleRepository.saveAll(articles);
        Assertions.assertEquals(11, articleRepository.count());
    }

    @Test
    public void searchArticle() {
        SearchHits<Article> searchHits = articleRepository.search("spring seasen");
        searchHits.forEach(print());
        Assertions.assertEquals(4, searchHits.getTotalHits());
    }
}
