package com.mihansweatpants.reminders.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReminderScheduler {

    private final ReminderBotService reminderBotService;
    private final ReminderService reminderService;

    public ReminderScheduler(ReminderBotService reminderBotService,
                             ReminderService reminderService) {
        this.reminderBotService = reminderBotService;
        this.reminderService = reminderService;
    }

    @Scheduled(fixedDelayString = "${scheduler.interval.run}")
    public void runSendScheduledReminders() {
        reminderService.getScheduledReminders()
                .parallelStream()
                .forEach(reminderBotService::sendReminder);
    }
}
