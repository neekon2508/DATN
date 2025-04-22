package admin.entity;

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

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "card_set")
    private CardSet cardSet;
    
    @NotNull
    private String frontText;

    private String frontImage;
    private String frontSound;

    @NotNull
    private String backText;

    private String backImage;
    private String backSound;

    private Boolean isLearned;
    private String learnedAt;
    private String createdAt;
    private String updatedAt;
}
