package com.mihansweatpants.reminders.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "reminders")
public class Reminder {
    @Id
    private String id;
    private String text;
    private Long chatId;
    private LocalDateTime scheduledAt;
    private Boolean isComplete = false;

    public Reminder setText(String text) {
        this.text = text;
        return this;
    }

    public Reminder setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
        return this;
    }

    public Reminder setComplete(Boolean complete) {
        this.isComplete = complete;
        return this;
    }

    public Reminder setChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }
}
