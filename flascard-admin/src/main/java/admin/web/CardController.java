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

}
