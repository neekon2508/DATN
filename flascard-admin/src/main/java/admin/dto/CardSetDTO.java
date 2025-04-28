package admin.dto;

import java.util.List;

import lombok.Data;

@Data
public class CardSetDTO {
    private Long id;
    private Long accountUserId; 
    private String name;
    private List<CardDTO> cards;

    public CardSetDTO(Long id, Long accountUserId, String name, List<CardDTO> cards) {
        this.id = id;
        this.name = name;
        this.accountUserId = accountUserId;
        cards = this.cards;
    }


}
