package admin.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import admin.entity.Card;
import admin.repository.CardRepository;

@RestController
@RequestMapping(path = "/api/card", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origins}")
public class APICardController {

    private CardRepository cardRepository;

    @Autowired
    public APICardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping("/get_all")
    public Iterable<Card> allCards() {
        return cardRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Card> cardById(@PathVariable("id") Long id) {
        Optional<Card> optCard = cardRepository.findById(id);
        if (optCard.isPresent()) 
            return new ResponseEntity<>(optCard.get(), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Card postCard(@RequestBody Card card) {
        return cardRepository.save(card);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    public Card putCard(@PathVariable("id") Long id, @RequestBody Card patch) {
        Card card = cardRepository.findById(id).get();
        if (patch.getFrontText() != null)
            card.setFrontText(patch.getFrontText());
        if (patch.getFrontImage() != null)
            card.setFrontImage(patch.getFrontImage());
        if (patch.getFrontSound() != null)
            card.setFrontSound(patch.getFrontSound());
        if (patch.getBackText() != null)
            card.setBackText(patch.getBackText());
        if (patch.getBackImage() != null)
            card.setBackImage(patch.getBackImage());
        if (patch.getBackSound() != null)
            card.setBackSound(patch.getBackSound());
        if (patch.getIsLearned() != null)
            card.setIsLearned(patch.getIsLearned());
        if (patch.getLearnedAt() != null)
            card.setLearnedAt(patch.getLearnedAt());
        if (patch.getCreatedAt() != null)
            card.setCreatedAt(patch.getCreatedAt());
        if (patch.getUpdatedAt() != null)
            card.setUpdatedAt(patch.getUpdatedAt());
        return cardRepository.save(card);
    
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable("id") Long id) {
        try {
            cardRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {}
    }
}
