package com.mihansweatpants.reminders.service.heroku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WakeUpDynoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WakeUpDynoService.class);

    private final String appUrl;

    public WakeUpDynoService(@Value("${heroku.app-url}") String appUrl) {
        this.appUrl = appUrl;
    }

    @Scheduled(fixedRateString = "${heroku.ping-rate}")
    void wakeUpDyno() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(appUrl).openConnection();
        connection.setRequestMethod("HEAD");
        LOGGER.info("Waking up heroku dyno {}", connection.getResponseCode());
        connection.disconnect();
    }
}
