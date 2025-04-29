package admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import admin.dto.CardDTO;
import admin.dto.CardSetDTO;
import admin.entity.AccountUser;
import admin.service.CardSetService;

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
    @PostMapping("/{id}/create_card")
    public String addCard(@PathVariable Long id, CardDTO cardDTO) {
        cardSetService.addCardToCardSet(id, cardDTO);
        return "redirect:/card_set/"+id;
    }
    @PostMapping("/update/{id}")
    public String updateCardSet(@PathVariable Long id, CardSetDTO cardSetDTO) {
        cardSetService.updateCardSet(id, cardSetDTO);
        return "redirect:/card_set";
    }
}
