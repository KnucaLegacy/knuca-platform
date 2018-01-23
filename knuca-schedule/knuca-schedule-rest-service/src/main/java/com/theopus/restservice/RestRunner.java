package com.theopus.restservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.theopus.repository.config")
@ComponentScan("com.theopus.restservice.controller")
public class RestRunner {
    public static void main(String[] args) {
        SpringApplication.run(RestRunner.class);
    }

}
