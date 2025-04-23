package admin.dto;

import lombok.Data;

@Data
public class CardSetDTO {
    private Long id;
    private String name;
    private Long accountUserId; // ID của AccountUser chứa CardSet này

    public CardSetDTO(Long id, String name, Long accountUserId) {
        this.id = id;
        this.name = name;
        this.accountUserId = accountUserId;
    }
}
