package admin.api;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import admin.dto.CardDTO;
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
    public Iterable<CardDTO> allCards() {
         return (Iterable<CardDTO>)((List<Card>)cardRepository.findAll()).stream().map(card->card.createDto()).toList();
       
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CardDTO> cardById(@PathVariable("id") Long id) {
        Optional<Card> optCard = cardRepository.findById(id);
        if (optCard.isPresent()) 
            return new ResponseEntity<>(optCard.get().createDto(), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getByText/{text}")
    public Iterable<CardDTO> cardByName(@PathVariable("text") String text) {
        var allCard = (List<CardDTO>)allCards();
        List<CardDTO> test = new ArrayList<>();
        for (CardDTO card : allCard) {
            if (card.getFrontText().contains(text) || card.getBackText().contains(text))
                test.add(card);
        }
        return (Iterable<CardDTO>) test;
    }
    @PostMapping(path = "/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Card postCard(@RequestBody Card card) {
        return cardRepository.save(card);
    }
    @PostMapping("/upload")
    public ResponseEntity<Map<String,String>> upload(@RequestParam("file") MultipartFile file) {
        Map<String,String> response = new HashMap<>();
        String directoryImageString = "images";
        String directorySoundString = "sounds";
        File directory;
        try {
            String contentType = file.getContentType();
            if (contentType.startsWith("image/")) {
             directory = new File(directoryImageString);
            } else if (contentType.startsWith("audio/")) {
                directory = new File(directorySoundString);
            } else {
                response.put("error", "Định dạng không hợp lệ");
                return ResponseEntity.badRequest().body(response);
            }
            if (!directory.exists())
                directory.mkdir();
            String fileName = UUID.randomUUID().toString()+"_"+file.getOriginalFilename();
            String filePath = directory.getAbsolutePath()+File.separator+ fileName;
            file.transferTo(new File(filePath));
             response.put("filePath", filePath);
             return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "Lỗi khi lưu file");
            return ResponseEntity.internalServerError().body(response);
        }
    }
    @PatchMapping(path = "/update/{id}", consumes = "application/json")
    public CardDTO putCard(@PathVariable("id") Long id, @RequestBody CardDTO patch) {
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
        return cardRepository.save(card).createDto();
    
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCard(@PathVariable("id") Long id) {
        try {
            CardDTO a = cardById(id).getBody();
            a.delete();
            cardRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {}
    }
}
