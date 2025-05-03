package admin.entity;

import java.io.File;

import admin.dto.CardDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardset_id")
    private CardSet cardSet;
    
    @NotNull
    private String frontText;

    private String frontImage;
    private String frontSound;

    @NotNull
    private String backText;

    private String backImage;
    private String backSound;

    @Column(name = "islearned")
    private Boolean isLearned;
    @Column(name="learnedat")
    private String learnedAt;

    @Column(name = "createdat")
    private String createdAt;
    @Column(name = "updatedat")
    private String updatedAt;

    public CardDTO createDto() {
        return new CardDTO(id, cardSet != null ? cardSet.getId() : 0, frontText, frontImage, frontSound, backText, backImage, backSound, isLearned, learnedAt, createdAt, updatedAt);
    }

    
}
