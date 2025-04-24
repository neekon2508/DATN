package com.example.flashcard.dto;

import java.util.List;

public class CardSetDTO {
    private Long id;
    private Long accountUserId;
    private String name;
    private List<CardDTO> cards;

    public CardSetDTO(Long id, Long accountUserId, String name, List<CardDTO> cards) {
        this.id = id;
        this.name = name;
        this.accountUserId = accountUserId;
        this.cards = cards ;
    }

    public Long getId() {
        return id;
    }

    public Long getAccountUserId() {
        return accountUserId;
    }

    public String getName() {
        return name;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
