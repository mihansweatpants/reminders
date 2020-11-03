package com.mihansweatpants.reminders.service;

import com.mihansweatpants.reminders.domain.Reminder;
import com.mihansweatpants.reminders.util.ReminderDateTimeUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@Service
public class ReminderBotService {

    private static final String REMIND_COMMAND = "напомни";
    private static final Pattern REMIND_COMMAND_PATTERN = Pattern.compile(REMIND_COMMAND, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    private final TelegramBot telegramBot;
    private final ReminderService reminderService;

    public ReminderBotService(TelegramBot telegramBot,
                              ReminderService reminderService) {
        this.telegramBot = telegramBot;
        this.reminderService = reminderService;
    }

    @PostConstruct
    void subscribeToUpdates() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processUpdate(Update update) {
        long chatId = update.message().chat().id();
        String messageText = update.message().text();

        if (REMIND_COMMAND_PATTERN.matcher(messageText).find()) {
            var reminderDate = ReminderDateTimeUtils.parseDate(messageText);
            var now = LocalDateTime.now();

            if (reminderDate.isBefore(now)) {
                telegramBot.execute(new SendMessage(chatId, "Попробуй другое время. Это уже прошло."));
            } else {
                reminderService.createReminder(messageText, chatId, reminderDate);
                telegramBot.execute(new SendMessage(chatId, "ок"));
            }
        }
    }

    public void sendReminder(Reminder reminder) {
        SendResponse response = telegramBot.execute(new SendMessage(reminder.getChatId(), reminder.getText()));

        if (response.isOk()) {
            reminderService.completeReminder(reminder);
        }
    }
}
