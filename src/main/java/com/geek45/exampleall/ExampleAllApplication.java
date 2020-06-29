package com.geek45.exampleall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ExampleAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleAllApplication.class, args);
    }

}
