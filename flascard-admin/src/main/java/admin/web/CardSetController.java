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
import admin.dto.CardSetDTO;
import admin.entity.AccountUser;
import admin.service.CardSetService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/card_set")
@SessionAttributes("admin")
public class CardSetController {
    private final CardSetService cardSetService;

    @Autowired
    public CardSetController(CardSetService cardSetService) {
        this.cardSetService = cardSetService;
    }

     @GetMapping()
    public String get(Model model) {
        model.addAttribute("card_sets", cardSetService.findAll());
        return "card_set";

    }
    @GetMapping("/{id}")
    public String getDetail(@PathVariable Long id, Model model) {
        CardSetDTO cardSetDTO = cardSetService.findCardSetDTOById(id);
        model.addAttribute("card_set", cardSetDTO);
        return "card_set_detail";
    }
    @GetMapping("/search/{name}")
    public String getByName(@PathVariable String name, Model model) {
        var cardSets = cardSetService.getByName(name);
        model.addAttribute("card_sets", cardSets);
        return "card_set";
    }
    @PostMapping("/{id}/create_card")
    public String addCard(@PathVariable Long id, 
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
        
        cardSetService.addCardToCardSet(id, cardDTO);
        return "redirect:/card_set/"+id;
    }
    @PostMapping("/update/{id}")
    public String updateCardSet(@PathVariable Long id, CardSetDTO cardSetDTO, HttpServletRequest request) {
        cardSetService.updateCardSet(id, cardSetDTO);
        
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/card_set");
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, HttpServletRequest request) {
        cardSetService.deleteCardSetById(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/card_set");
    }
}
