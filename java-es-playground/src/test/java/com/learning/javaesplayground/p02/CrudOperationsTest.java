package com.learning.javaesplayground.p02;

import com.learning.javaesplayground.AbstractTest;
import com.learning.javaesplayground.p02.entity.Employee;
import com.learning.javaesplayground.p02.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;

import java.util.List;
import java.util.stream.IntStream;

public class CrudOperationsTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(CrudOperationsTest.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void crud() {
        Employee employee = createEmployee(1, "shubham", 28);

        employeeRepository.save(employee);
        printAll();

        employee = employeeRepository.findById(1)
                .orElseThrow(RuntimeException::new);
        Assertions.assertEquals("shubham", employee.getName());
        Assertions.assertEquals(28, employee.getAge());

        employee.setAge(32);
        employee = employeeRepository.save(employee);
        printAll();

        Assertions.assertEquals(32, employee.getAge());

        employeeRepository.deleteById(1);
        printAll();

        Assertions.assertTrue(employeeRepository.findById(1).isEmpty());

    }

    @Test
    public void bulkCrud() {
        List<Employee> employeeList =  IntStream.rangeClosed(1, 10)
                .mapToObj(i -> createEmployee(i, "name-" + i, 20 + i))
                .toList();
        employeeRepository.saveAll(employeeList);
        printAll();

        Assertions.assertEquals(10, employeeRepository.count());

        List<Integer> ids = List.of(2, 4, 6);
        Iterable<Employee> it = employeeRepository.findAllById(ids);
        employeeList = Streamable.of(it).toList();

        Assertions.assertEquals(3, employeeList.size());

        employeeList.forEach(e -> e.setAge(e.getAge() + 10));
        employeeRepository.saveAll(employeeList);
        printAll();

        employeeRepository.findAllById(ids)
                .forEach(e -> Assertions.assertEquals(e.getId() + 30, e.getAge()));

        employeeRepository.deleteAllById(ids);
        printAll();

        Assertions.assertEquals(7, employeeRepository.count());

    }

    private Employee createEmployee(int id, String name, int age) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setAge(age);
        return employee;
    }

    private void printAll() {
        employeeRepository
                .findAll()
                .forEach(e -> log.info("employee: {}", e));
    }

}
