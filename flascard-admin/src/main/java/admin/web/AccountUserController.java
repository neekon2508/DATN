package admin.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import admin.dto.AccountUserDTO;
import admin.dto.CardSetDTO;
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
    public String getDetail(@PathVariable Long id,Model model) {
        AccountUserDTO user = accountUserService.findAccountUserDTOById(id);
        model.addAttribute("card_sets", user.getCardSets());
        model.addAttribute("account_user_id", id);
        return "account_user_detail";
    }
    @GetMapping("/search/{username}")
    public String getByUserName(@PathVariable String username, Model model) {
        Iterable<AccountUserDTO> users = accountUserService.findAccountUserDTOByUserName(username);
        model.addAttribute("account_users", users);
        return "account_user";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        accountUserService.deleteAccountUserById(id);
        return "redirect:/account_user";
    }
    @PostMapping("/{id}/create_cardset")
    public String addCardSet(@PathVariable Long id, CardSetDTO cardSetDTO) {
        System.out.println(cardSetDTO);
        accountUserService.addCardSetToAccountUser(id, cardSetDTO);
        return "redirect:/account_user/"+id;
    }
    @PostMapping("/create")
    public String addAccountUser( AccountUser accountUser) {
        accountUserService.addAccountUser(accountUser);
        return "redirect:/account_user";
    }
    @PostMapping("/update/{id}")
    public String updateAccountUser(@PathVariable Long id, AccountUser accountUser) {
        accountUserService.updateAccountUser(id, accountUser.createDTO());
        return "redirect:/account_user";
    }
 
}
