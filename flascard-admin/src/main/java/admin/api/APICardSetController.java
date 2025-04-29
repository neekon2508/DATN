package admin.api;

import java.util.List;
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

import admin.dto.AccountUserDTO;
import admin.dto.CardDTO;
import admin.dto.CardSetDTO;
import admin.entity.AccountUser;
import admin.entity.Card;
import admin.entity.CardSet;
import admin.repository.CardSetRepository;

@RestController
@RequestMapping(path = "/api/card_set", produces = "application/json")
@CrossOrigin(origins = "${app.cors.origins}")
public class APICardSetController {

    private CardSetRepository cardSetRepository;
    @Autowired
    public APICardSetController(CardSetRepository cardSetRepository) {
        this.cardSetRepository = cardSetRepository;
    }

    @GetMapping("/get_all")
    public Iterable<CardSetDTO> allCardSets() {
       
       return  (Iterable<CardSetDTO>)((List<CardSet>)cardSetRepository.findAll()).stream().map(cardset->cardset.createDTO()).toList();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<CardSetDTO> cardSetById(@PathVariable("id") Long id) {
        Optional<CardSet> optCardSet = cardSetRepository.findById(id);

        if (optCardSet.isPresent())
            return new ResponseEntity<>(optCardSet.get().createDTO(), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping(path="/create_card/{id}", consumes ="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CardSetDTO addCardtoCardSet(@PathVariable Long id, @RequestBody CardDTO cardDTO) {
        CardSet cardSet = cardSetRepository.findById(id).get();
        Card newCard = new Card();
        newCard.setFrontText(cardDTO.getFrontText());
        newCard.setFrontImage(cardDTO.getFrontImage());
        newCard.setFrontSound(cardDTO.getFrontSound());
        newCard.setBackText(cardDTO.getBackText());
        newCard.setBackImage(cardDTO.getBackImage());
        newCard.setBackSound(cardDTO.getBackSound());

        newCard.setCardSet(cardSet);
        cardSet.getCards().add(newCard);
        System.out.println(newCard.createDto());
        System.out.println(cardSet.createDTO());

        var t =  cardSetRepository.save(cardSet).createDTO();
        System.out.println(t);
        return t;
    }
    @PostMapping(path = "/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CardSet postCardSet(@RequestBody CardSet cardSet) {
        return cardSetRepository.save(cardSet);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    public CardSetDTO putCardSet(@PathVariable("id") Long id, @RequestBody CardSetDTO patch) {
        CardSet cardSet = cardSetRepository.findById(id).get();
        if (patch.getName() != null)
            cardSet.setName(patch.getName());
        return cardSetRepository.save(cardSet).createDTO();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCardSet(@PathVariable("id") Long id) {
        try {
            cardSetRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {}
    }
}
