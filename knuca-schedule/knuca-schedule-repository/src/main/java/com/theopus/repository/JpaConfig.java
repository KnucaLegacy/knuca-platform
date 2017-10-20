package com.theopus.repository;


import com.theopus.entity.schedule.Subject;
import com.theopus.repository.jparepo.SubjectRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EntityScan("com.theopus.entity.schedule")
public class JpaConfig {


}
