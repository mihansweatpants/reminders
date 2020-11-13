package com.mihansweatpants.reminders.service;

import com.mihansweatpants.reminders.domain.ChatSettings;
import com.mihansweatpants.reminders.repository.ChatSettingsRepository;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Optional;

@Service
public class ChatSettingsService {

    private final ChatSettingsRepository chatSettingsRepository;

    public ChatSettingsService(ChatSettingsRepository chatSettingsRepository) {
        this.chatSettingsRepository = chatSettingsRepository;
    }

    public ChatSettings createChatSettings(long chatId, ZoneId zoneId) {
        return chatSettingsRepository.findByChatId(chatId)
                .map(chatSettings -> chatSettingsRepository.save(chatSettings.setTimezoneId(zoneId)))
                .orElseGet(() -> chatSettingsRepository.save(new ChatSettings().setChatId(chatId).setTimezoneId(zoneId)));
    }

    public Optional<ChatSettings> getChatSettings(long chatId) {
        return chatSettingsRepository.findByChatId(chatId);
    }
}
