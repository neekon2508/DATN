package com.example.flashcard.dto;

import java.time.LocalDateTime;

public class CardDTO {

    private Long id;

    private Long cardSetId;

    private String frontText;

    private String frontImage;

    private String frontSound;

    private String backText;

    private String backImage;

    private String backSound;

    private Boolean isLearned;

    private String learnedAt;

    private String createdAt;

    private String updatedAt;

    public CardDTO(Long id, Long cardSetId, String frontText, String frontImage, String frontSound, String backText,
                   String backImage, String backSound, Boolean isLearned, String learnedAt, String createdAt,
                   String updatedAt) {
        this.id = id;
        this.cardSetId = cardSetId;
        this.frontText = frontText;
        this.frontImage = frontImage;
        this.frontSound = frontSound;
        this.backText = backText;
        this.backImage = backImage;
        this.backSound = backSound;
        this.isLearned = isLearned;
        this.learnedAt = learnedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public CardDTO() {
        isLearned = false;
        learnedAt = LocalDateTime.now().toString();
        createdAt = LocalDateTime.now().toString();
        updatedAt = LocalDateTime.now().toString();
    }
    public Long getId() {
        return id;
    }

    public Long getCardSetId() {
        return cardSetId;
    }

    public String getFrontText() {
        return frontText;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public String getFrontSound() {
        return frontSound;
    }

    public String getBackText() {
        return backText;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setFrontText(String frontText) {
        this.frontText = frontText;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public void setFrontSound(String frontSound) {
        this.frontSound = frontSound;
    }

    public void setBackText(String backText) {
        this.backText = backText;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public void setBackSound(String backSound) {
        this.backSound = backSound;
    }

    public void setLearned(Boolean learned) {
        isLearned = learned;
    }

    public void setLearnedAt(String learnedAt) {
        this.learnedAt = learnedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
