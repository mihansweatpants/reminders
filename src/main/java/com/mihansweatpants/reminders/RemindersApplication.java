package com.mihansweatpants.reminders;

import com.mihansweatpants.reminders.repository.ReminderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableMongoRepositories
@EnableScheduling
@SpringBootApplication
public class RemindersApplication {

    public static void main(String[] args) {
        SpringApplication.run(RemindersApplication.class, args);
    }

    @Bean
    CommandLineRunner test(ReminderRepository reminderRepository) {
        return (args) -> {
        };
    }

}
