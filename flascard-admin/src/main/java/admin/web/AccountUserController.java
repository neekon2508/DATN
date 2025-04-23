package admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import admin.dto.AccountUserDTO;
import admin.entity.AccountUser;
import admin.entity.CardSet;
import admin.repository.AccountUserRepository;
import admin.service.AccountUserService;

@Controller
@RequestMapping("/account_user")
@SessionAttributes("admin")
public class AccountUserController {
 
    private final AccountUserService accountUserService;

    @Autowired
    public AccountUserController(AccountUserService accountUserService ) {
        this.accountUserService = accountUserService;

    }
    @GetMapping()
    public String get(Model model) {


        model.addAttribute("account_users", accountUserService.findAll());

        return "account_user";

    }
    @GetMapping("/{id}")
    public String getDetail(@PathVariable String id,Model model) {
        AccountUserDTO user = accountUserService.findAccountUserById(id);
        model.addAttribute("card_sets", user.getCardSets());
        return "account_user_detail";
    }
    @PostMapping("/create")
    public String addAccountUser( AccountUser accountUser) {
        accountUserService.addAccountUser(accountUser);
        return "redirect:/account_user";
    }
}
