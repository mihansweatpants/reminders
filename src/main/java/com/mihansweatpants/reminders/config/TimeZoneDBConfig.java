package com.mihansweatpants.reminders.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TimeZoneDBConfig {

    @Bean
    public RestTemplate timeZoneDBRestTemplate(@Value("${timezonedb.api-url}") String apiUrl) {
        return new RestTemplateBuilder()
                .rootUri(apiUrl)
                .build();
    }

}
