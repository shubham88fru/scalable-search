package com.learning.javaesplayground.p02.repository;

import com.learning.javaesplayground.p02.entity.Employee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends ElasticsearchRepository<Employee, Integer> {
}
