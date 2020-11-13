package com.mihansweatpants.reminders.repository;

import com.mihansweatpants.reminders.domain.ChatSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatSettingsRepository extends MongoRepository<ChatSettings, String> {
    Optional<ChatSettings> findByChatId(Long chatId);
}
