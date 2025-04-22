package admin.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import admin.entity.AccountUser;

@Controller
@RequestMapping("/card_set")
public class CardSetController {

     @GetMapping()
    public String get(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountUser admin = (AccountUser) authentication.getPrincipal();
        model.addAttribute("username", admin.getUsername());
        return "card_set";

    }
}
