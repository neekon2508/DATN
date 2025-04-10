package com.example.flashcard.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Card {
    private long id;
    private String frontText;
    private String behindText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Card(long id, String frontText, String behindText) {
        this.id = id;
        this.frontText = frontText;
        this.behindText = behindText;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}
