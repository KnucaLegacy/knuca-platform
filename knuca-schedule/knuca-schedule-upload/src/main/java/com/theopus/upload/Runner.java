package com.theopus.upload;

import com.theopus.repository.config.DataBaseServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = DataBaseServiceConfig.class)
@ComponentScan("com.theopus.upload.service")
public class Runner {

    public static void main(String[] args) {
        SpringApplication.run(Runner.class);
    }

}
