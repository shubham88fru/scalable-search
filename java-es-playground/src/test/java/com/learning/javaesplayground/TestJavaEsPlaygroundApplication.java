package com.learning.javaesplayground;

import org.springframework.boot.SpringApplication;

public class TestJavaEsPlaygroundApplication {

    public static void main(String[] args) {
        SpringApplication.from(JavaEsPlaygroundApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
