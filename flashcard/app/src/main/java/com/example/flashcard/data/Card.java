package com.example.flashcard.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Card {

    private String frontText;
    private String behindText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Card(String frontText, String behindText) {

        this.frontText = frontText;
        this.behindText = behindText;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBehindText() {
        return behindText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }



}
