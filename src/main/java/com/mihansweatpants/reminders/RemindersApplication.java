package com.mihansweatpants.reminders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories
@EnableScheduling
@SpringBootApplication
public class RemindersApplication {
    public static void main(String[] args) {
        SpringApplication.run(RemindersApplication.class, args);
    }
}
