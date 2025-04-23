package admin.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

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
}
