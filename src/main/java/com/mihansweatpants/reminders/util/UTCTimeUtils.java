package com.mihansweatpants.reminders.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class UTCTimeUtils {
    public static LocalDateTime now() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime toUTC(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }
}

