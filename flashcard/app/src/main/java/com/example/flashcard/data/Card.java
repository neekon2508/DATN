package com.example.flashcard.data;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Card {

    private int id;
    private String frontText;
    private String backText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Card(int id, String frontText, String backText) {

        this.id = id;
        this.frontText = frontText;
        this.backText = backText;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public String getFrontText() {
        return frontText;
    }

    public String getBackText() {
        return backText;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }



}
