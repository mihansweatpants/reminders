package com.mihansweatpants.reminders.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_settings")
public class ChatSettings {
    @Id
    private String id;
    private Long chatId;
    private ZoneId timezoneId;

    public ChatSettings setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public ChatSettings setTimezoneId(ZoneId timezoneId) {
        this.timezoneId = timezoneId;
        return this;
    }
}
