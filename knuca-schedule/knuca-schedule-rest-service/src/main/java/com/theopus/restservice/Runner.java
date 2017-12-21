package com.theopus.restservice;

import com.theopus.entity.schedule.Room;
import com.theopus.repository.service.RoomService;
import com.theopus.repository.service.SimpleService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Runner {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(Runner.class);
        RoomService roomSimpleService = run.getBean(RoomService.class);
    }
}
