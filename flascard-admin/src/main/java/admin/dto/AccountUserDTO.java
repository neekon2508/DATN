package admin.dto;

import java.util.List;

import lombok.Data;
@Data
public class AccountUserDTO {
    private Long id;
    private String username;
    private String password;
    private String authority;
    private List<CardSetDTO> cardSets;

    public AccountUserDTO(Long id, String username, String password, String authority, List<CardSetDTO> cardSets) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.cardSets = cardSets;
        this.authority = authority;
    }
    public void delete() {
        if (!getCardSets().isEmpty())
            for (CardSetDTO cardSetDTO : getCardSets()) 
                if (!cardSetDTO.getCards().isEmpty())
                 for (CardDTO cardDTO : cardSetDTO.getCards())
                    if (cardDTO != null)
                        cardDTO.delete();
    }

    // Getters và Setters
}
