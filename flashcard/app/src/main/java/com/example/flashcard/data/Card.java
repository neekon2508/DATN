package com.example.flashcard.data;

import com.example.flashcard.dto.CardDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Card {

    private int id;
    private String frontText;
    private String backText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String frontImage;

    private String frontSound;

    private String backImage;

    private String backSound;

    private Boolean isLearned;

    private String learnedAt;

public Card(CardDTO cardDTO) {
    id = cardDTO.getId().intValue();
    frontText = cardDTO.getFrontText();
    frontImage = cardDTO.getFrontImage();
    frontSound = cardDTO.getFrontSound();
    backText = cardDTO.getBackText();
    backImage = cardDTO.getBackImage();
    backSound = cardDTO.getBackSound();
    isLearned = cardDTO.getLearned();

}
    public Card(int id, String frontText, String backText) {

        this.id = id;
        this.frontText = frontText;
        this.backText = backText;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    public int getId() {return id;}

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

    public String getFrontImage() {
        return frontImage;
    }

    public String getFrontSound() {
        return frontSound;
    }

    public String getBackImage() {
        return backImage;
    }

    public String getBackSound() {
        return backSound;
    }

    public Boolean getLearned() {
        return isLearned;
    }

    public String getLearnedAt() {
        return learnedAt;
    }
}
