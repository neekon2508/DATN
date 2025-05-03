package admin.dto;

import java.io.File;

import admin.entity.CardSet;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
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
   public void delete() {
        if (!frontImage.isEmpty())
            (new File(frontImage)).delete();
        if (!backImage.isEmpty())
            (new File(backImage)).delete();
        if (!backImage.isEmpty())
            (new File(backImage)).delete();
        if (!backSound.isEmpty())
            (new File(backSound)).delete();
    }
    
}
