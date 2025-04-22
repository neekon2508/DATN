package admin.web;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import admin.data.AccountUserForm;
import admin.repository.AccountUserRepository;

@Controller
@RequestMapping("/create_account_user")
public class AccountUserFormController {

    private AccountUserRepository userRepo;
    private PasswordEncoder passwordEncoder;

    public AccountUserFormController(AccountUserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = encoder;
    }

    @GetMapping
    public String createAccountUserForm() {
        return "create_account_user_form";
    }

    @PostMapping
    public String process(AccountUserForm form) {
        userRepo.save(form.toUser(passwordEncoder));
        return "redirect:/";
    }

}
