package com.mihansweatpants.reminders.util;

import com.mihansweatpants.reminders.exception.ReminderDateParsingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReminderDateTimeUtils {
    private static final Pattern TIME_FULL_PATTERN = Pattern.compile("в ([01]?[0-9]|2[0-3]):[0-5][0-9]", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern TIME_SHORT_PATTERN = Pattern.compile("в ([01]?[0-9]|2[0-3])", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Pattern TOMORROW_PATTERN = Pattern.compile("завтра", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    private static final Pattern DAY_AFTER_TOMORROW_PATTERN = Pattern.compile("послезавтра", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private static final Map<String, Month> MONTH_NAME_DICTIONARY = Map.ofEntries(
            Map.entry("января", Month.JANUARY),
            Map.entry("марта", Month.MARCH),
            Map.entry("апреля", Month.APRIL),
            Map.entry("мая", Month.MAY),
            Map.entry("июня", Month.JUNE),
            Map.entry("июля", Month.JULY),
            Map.entry("августа", Month.AUGUST),
            Map.entry("сентября", Month.SEPTEMBER),
            Map.entry("октября", Month.OCTOBER),
            Map.entry("ноября", Month.NOVEMBER),
            Map.entry("декабря", Month.DECEMBER)
    );

    private static final Pattern DAY_AND_MONTH_NAME_PATTERN = Pattern.compile(
            "([1-9]|[12]\\d|3[0-1]) " + String.format("(%s)", String.join("|", MONTH_NAME_DICTIONARY.keySet())),
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
    );

    public static LocalDateTime parseDate(String text) throws ReminderDateParsingException {
        Matcher timeFullMatcher = TIME_FULL_PATTERN.matcher(text);
        Matcher timeShortMatcher = TIME_SHORT_PATTERN.matcher(text);

        LocalTime time;
        if (timeFullMatcher.find()) {
            String timeString = timeFullMatcher.group(0).split(" ")[1];
            time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        } else if (timeShortMatcher.find()) {
            String hourString = timeShortMatcher.group(0).split(" ")[1];
            time = LocalTime.of(Integer.parseInt(hourString), 0);
        } else {
            throw new ReminderDateParsingException("Could not parse time");
        }

        LocalDate date = null;
        LocalDate currentDate = UTCTimeUtils.now().toLocalDate();
        Matcher dayAndMonthMatcher = DAY_AND_MONTH_NAME_PATTERN.matcher(text);
        if (dayAndMonthMatcher.find()) {
            int day = Integer.parseInt(dayAndMonthMatcher.group(1));
            Month month = MONTH_NAME_DICTIONARY.get(dayAndMonthMatcher.group(2));
            int year = currentDate.getYear();

            LocalDate dateToSet = LocalDate.of(year, month, day);
            if (currentDate.isAfter(dateToSet)) {
                dateToSet = dateToSet.plusYears(1);
            }

            date = dateToSet;
        } else if (TOMORROW_PATTERN.matcher(text).find()) {
            date = UTCTimeUtils.now().toLocalDate().plusDays(1);
        } else if (DAY_AFTER_TOMORROW_PATTERN.matcher(text).find()) {
            date = UTCTimeUtils.now().toLocalDate().plusDays(2);
        } else {
            date = currentDate;
        }

        return LocalDateTime.of(date, time);
    }
}
