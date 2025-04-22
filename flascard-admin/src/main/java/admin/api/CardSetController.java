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

import admin.entity.CardSet;
import admin.repository.CardSetRepository;

@RestController
@RequestMapping(path = "/api/card_set", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origins}")
public class CardSetController {

    private CardSetRepository cardSetRepository;
    @Autowired
    public CardSetController(CardSetRepository cardSetRepository) {
        this.cardSetRepository = cardSetRepository;
    }

    @GetMapping("/get_all")
    public Iterable<CardSet> allCardSets() {
        return cardSetRepository.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CardSet> cardSetById(@PathVariable("id") Long id) {
        Optional<CardSet> optCardSet = cardSetRepository.findById(id);

        if (optCardSet.isPresent())
            return new ResponseEntity<>(optCardSet.get(), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping(path = "/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CardSet postCardSet(@RequestBody CardSet cardSet) {
        return cardSetRepository.save(cardSet);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    public CardSet putCardSet(@PathVariable("id") Long id, @RequestBody CardSet patch) {
        CardSet cardSet = cardSetRepository.findById(id).get();
        if (patch.getName() != null)
            cardSet.setName(patch.getName());
        return cardSetRepository.save(cardSet);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCardSet(@PathVariable("id") Long id) {
        try {
            cardSetRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {}
    }
}
