package com.mihansweatpants.reminders.service;

import com.mihansweatpants.reminders.domain.Reminder;
import com.mihansweatpants.reminders.exception.ReminderDateParsingException;
import com.mihansweatpants.reminders.service.timezonedb.client.TimeZoneDBClient;
import com.mihansweatpants.reminders.util.ReminderDateTimeUtils;
import com.mihansweatpants.reminders.util.UTCTimeUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Location;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ReminderBotService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReminderBotService.class);

    private static final String REMIND_COMMAND = "напомни";
    private static final Pattern REMIND_COMMAND_PATTERN = Pattern.compile(REMIND_COMMAND, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final TelegramBot telegramBot;
    private final ReminderService reminderService;
    private final ChatSettingsService chatSettingsService;
    private final TimeZoneDBClient timeZoneDBClient;

    public ReminderBotService(TelegramBot telegramBot,
                              ReminderService reminderService,
                              ChatSettingsService chatSettingsService,
                              TimeZoneDBClient timeZoneDBClient) {
        this.telegramBot = telegramBot;
        this.reminderService = reminderService;
        this.chatSettingsService = chatSettingsService;
        this.timeZoneDBClient = timeZoneDBClient;
    }

    @PostConstruct
    void subscribeToUpdates() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processUpdate(Update update) {
        try {
            LOGGER.info("Processing update {}", update);

            Optional.ofNullable(update.message()).ifPresent(message -> {
                Optional.ofNullable(message.location()).ifPresent(location -> processLocationMessage(message.chat().id(), location));
                Optional.ofNullable(message.text()).ifPresent(text -> processTextMessage(message.chat().id(), text));
            });

            Optional.ofNullable(update.editedMessage()).ifPresent(message -> {
                Optional.ofNullable(message.text()).ifPresent(text -> processTextMessage(message.chat().id(), text));
            });
        }
        catch (Exception e) {
            LOGGER.error("Could not process update {}", update);
        }
    }

    private void processLocationMessage(long chatId, Location location) {
        var timezoneInfo = timeZoneDBClient.getTimeZoneByLocation(location.latitude(), location.longitude());
        chatSettingsService.createChatSettings(chatId, ZoneId.of(timezoneInfo.getZoneName()));

        telegramBot.execute(new SendMessage(chatId, String.format("Выбран часовой пояс %s. Теперь можно создавать напоминания", timezoneInfo.getZoneName())));
    }

    private void processTextMessage(long chatId, String text) {
        if (REMIND_COMMAND_PATTERN.matcher(text).find()) {
            chatSettingsService.getChatSettings(chatId)
                    .ifPresentOrElse(
                            (chatSettings) -> {
                                try {
                                    var configuredTimeZoneId = chatSettings.getTimezoneId();
                                    var scheduledDate = UTCTimeUtils.toUTC(ReminderDateTimeUtils.parseDate(text, configuredTimeZoneId));

                                    if (scheduledDate.isBefore(UTCTimeUtils.now())) {
                                        telegramBot.execute(new SendMessage(chatId, "Попробуй другое время. Это уже прошло."));
                                    } else {
                                        reminderService.createReminder(text, chatId, scheduledDate);
                                        telegramBot.execute(new SendMessage(chatId, "ок"));
                                    }
                                } catch (ReminderDateParsingException e) {
                                    telegramBot.execute(new SendMessage(chatId, "Не получилось распарсить время. Попробуй еще раз в формате \"напомни [затра|послезавтра|dd (января|февраля|...)] в hh[:MM] \""));
                                    LOGGER.error("Error trying to parse reminder date from message \"{}\" {}", text, e);
                                } catch (Exception e) {
                                    telegramBot.execute(new SendMessage(chatId, "Что-то где-то пошло не так :("));
                                    LOGGER.error("Error trying to create reminder from message \"{}\" {}", text, e);
                                }
                            },
                            () -> telegramBot.execute(new SendMessage(chatId, "Пришли свою геолокацию (не работает на десктопе ¯\\_(ツ)_/¯)")));
        }
    }

    public void sendReminder(Reminder reminder) {
        LOGGER.info("Sending reminder {}", reminder);
        SendResponse response = telegramBot.execute(new SendMessage(reminder.getChatId(), reminder.getText()));

        if (response.isOk()) {
            reminderService.completeReminder(reminder);
        }
    }
}
