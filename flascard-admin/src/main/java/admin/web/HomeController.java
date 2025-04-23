package admin.web;


import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import admin.entity.AccountUser;

@Controller
@SessionAttributes("admin")
public class HomeController {

    @ModelAttribute(name = "admin")
    public AccountUser user() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountUser admin = (AccountUser) authentication.getPrincipal();
        return admin;
    }
    @GetMapping("/")
    public String home() {

        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // AccountUser admin = (AccountUser) authentication.getPrincipal();
        // model.addAttribute("username", admin.getUsername());
        return "home";

    }

   
}
