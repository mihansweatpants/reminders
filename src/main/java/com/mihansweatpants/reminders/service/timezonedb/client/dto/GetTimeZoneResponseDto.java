package com.mihansweatpants.reminders.service.timezonedb.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetTimeZoneResponseDto {
    private final String countryName;

    private final String zoneName;

    private final Long timestamp;

    private final String formattedDateTime;

    public GetTimeZoneResponseDto(@JsonProperty("countryName") String countryName,
                                  @JsonProperty("zoneName") String zoneName,
                                  @JsonProperty("timestamp") Long timestamp,
                                  @JsonProperty("formatted") String formattedDateTime) {
        this.countryName = countryName;
        this.zoneName = zoneName;
        this.timestamp = timestamp;
        this.formattedDateTime = formattedDateTime;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
