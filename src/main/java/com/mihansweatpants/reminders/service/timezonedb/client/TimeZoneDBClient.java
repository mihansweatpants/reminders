package com.mihansweatpants.reminders.service.timezonedb.client;

import com.mihansweatpants.reminders.service.timezonedb.client.dto.GetTimeZoneResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class TimeZoneDBClient {

    private final RestTemplate timeZoneDBRestTemplate;

    private final String apiKey;

    public TimeZoneDBClient(RestTemplate timeZoneDBRestTemplate, @Value("${timezonedb.api-key}") String apiKey) {
        this.timeZoneDBRestTemplate = timeZoneDBRestTemplate;
        this.apiKey = apiKey;
    }

    public GetTimeZoneResponseDto getTimeZoneByLocation(double latitude, double longitude) {
        return timeZoneDBRestTemplate
                .getForEntity("/get-time-zone?key={key}&format=json&by=position&lat={lat}&lng={lng}",
                        GetTimeZoneResponseDto.class,
                        Map.of("key", apiKey, "lat", latitude, "lng", longitude))
                .getBody();
    }
}
