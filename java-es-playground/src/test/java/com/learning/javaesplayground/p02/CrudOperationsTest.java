package com.learning.javaesplayground.p02;

import com.learning.javaesplayground.AbstractTest;
import com.learning.javaesplayground.p02.entity.Employee;
import com.learning.javaesplayground.p02.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
