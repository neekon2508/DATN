package com.example.flashcard.dto;

import java.util.List;

public class AccountUserDTO {
    private Long id;
    private String username;
    private String password;
    private String authority;
    private List<CardSetDTO> cardSets;

    public AccountUserDTO() {
    }

    public AccountUserDTO(Long id, String username, String password, String authority, List<CardSetDTO> cardSets) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.cardSets = cardSets;
        this.authority = authority;
        this.cardSets = cardSets;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
// Getters và Setters

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthority() {
        return authority;
    }

    public List<CardSetDTO> getCardSets() {
        return cardSets;
    }
}
