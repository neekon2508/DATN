package admin.web;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import admin.dto.CardDTO;
import admin.entity.AccountUser;
import admin.service.CardService;

@Controller
@RequestMapping("/card")
@SessionAttributes("admin")
public class CardController {

    private final CardService cardService;
    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    @GetMapping()
    public String get(Model model) {
        model.addAttribute("cards", cardService.findAll());
        return "card";
    }
    @GetMapping("/search/{text}")
    public String getByText(@PathVariable String text, Model model) {
        var cards = cardService.getByText(text);
        model.addAttribute("cards", cards);
        return "card";
    }
    @PostMapping("/update/{id}")
    public String updateCard(@PathVariable Long id, 
        @RequestParam("image_of_front") MultipartFile image_of_front,
        @RequestParam("image_of_back") MultipartFile image_of_back,
        @RequestParam("sound_of_front") MultipartFile sound_of_front,
        @RequestParam("sound_of_back") MultipartFile sound_of_back,
        CardDTO cardDTO) {

        String imagesDirectory = "images";
        String soundDirectory = "sounds";
        try {
            File directory = new File(imagesDirectory);
            if (!directory.exists())
                directory.mkdir();
            String fileName = "",filePath="";
            if (!image_of_front.isEmpty())
            {
                fileName = UUID.randomUUID().toString()+"_"+image_of_front.getOriginalFilename();
                filePath = directory.getAbsolutePath()+File.separator+ fileName;
                image_of_front.transferTo(new File(filePath));
                cardDTO.setFrontImage(filePath);
            }
            if (!image_of_back.isEmpty())
            {
                fileName = UUID.randomUUID().toString()+"_"+image_of_back.getOriginalFilename();
                filePath = directory.getAbsolutePath()+File.separator+ fileName;
                image_of_back.transferTo(new File(filePath));
                cardDTO.setBackImage(filePath);
            }

           
            directory = new File(soundDirectory);
            if (!directory.exists())
                directory.mkdir();

            if (!sound_of_front.isEmpty())
            {
                fileName = UUID.randomUUID().toString()+"_"+sound_of_front.getOriginalFilename();
                filePath = directory.getAbsolutePath()+File.separator+ fileName;
                sound_of_front.transferTo(new File(filePath));
                cardDTO.setFrontSound(filePath);
            }
            if (!sound_of_back.isEmpty())
            {
                fileName = UUID.randomUUID().toString()+"_"+sound_of_back.getOriginalFilename();
                filePath = directory.getAbsolutePath()+File.separator+ fileName;
                sound_of_back.transferTo(new File(filePath));
                cardDTO.setBackSound(filePath);
            }

        } catch (IOException e) {
        }

        cardService.updateCard(id, cardDTO);
        return "redirect:/card";
    }
    @GetMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return "redirect:/card";
    }

}
