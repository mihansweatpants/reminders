package com.mihansweatpants.reminders.service;

import com.mihansweatpants.reminders.domain.Reminder;
import com.mihansweatpants.reminders.repository.ReminderRepository;
import com.mihansweatpants.reminders.util.UTCTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public void createReminder(String text, Long chatId, LocalDateTime localDateTime) {
        reminderRepository.save(
                new Reminder()
                        .setText(text)
                        .setChatId(chatId)
                        .setScheduledAt(localDateTime)
        );
    }

    public List<Reminder> getScheduledReminders() {
        return reminderRepository.findAllByIsCompleteFalseAndScheduledAtBefore(UTCTimeUtils.now());
    }

    public void completeReminder(Reminder reminder) {
        reminderRepository.save(reminder.setComplete(true));
    }
}
