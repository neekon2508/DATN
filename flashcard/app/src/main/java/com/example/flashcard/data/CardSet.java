package com.example.flashcard.data;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CardSet {
    private List<Card> cardSet;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CardSet() {
        cardSet = new ArrayList<>();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public List<Card> getCardSet() {
        return cardSet;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }


}
