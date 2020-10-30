package com.mihansweatpants.reminders.repository;

import com.mihansweatpants.reminders.domain.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends MongoRepository<Reminder, String> {
    List<Reminder> findAllByIsCompleteFalseAndScheduledAtBefore(LocalDateTime time);
}
