package com.learning.javaesplayground.p05.repository;

import com.learning.javaesplayground.p05.entity.Garment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GarmentRepository extends ElasticsearchRepository<Garment, String> {
}
