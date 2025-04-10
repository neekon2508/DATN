package com.example.flashcard.data;

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
}
