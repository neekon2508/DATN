package admin.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import admin.entity.AccountUser;
import admin.repository.AccountUserRepository;

@RestController
@RequestMapping(path = "/api/account_user", produces = "application/json")
@CrossOrigin(origins= "${app.cors.origins}")
// @CrossOrigin(origins= "http://localhost:8080")
public class APIAccountUserController {
    private AccountUserRepository accountUserRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public APIAccountUserController(AccountUserRepository accountUserRepository, PasswordEncoder passwordEncoder) {
        this.accountUserRepository = accountUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/get_all")
    public Iterable<AccountUser> allAccountUsers() {
        return accountUserRepository.findAll();
    }
    @GetMapping("/get/{id}")
    public ResponseEntity<AccountUser> accountUserById(@PathVariable("id") Long id) {
        Optional<AccountUser> optAccountUser = accountUserRepository.findById(id);

        if(optAccountUser.isPresent())
            return new ResponseEntity<>(optAccountUser.get(), HttpStatus.OK);
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @PostMapping(path="/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountUser postAccountUser(@RequestBody AccountUser accountUser) {
        return accountUserRepository.save(new AccountUser(
            accountUser.getUsername(), passwordEncoder.encode(accountUser.getPassword()), accountUser.getAuthority()) 
        );
    }
    @PatchMapping(path="/update/{id}", consumes = "application/json")
    public AccountUser putAccountUser(@PathVariable("id") Long id, @RequestBody AccountUser patch) {
        AccountUser accountUser = accountUserRepository.findById(id).get();
        if (patch.getUsername() != null)
          accountUser.setUsername(patch.getUsername());
        return accountUserRepository.save(accountUser);
    }
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountUser(@PathVariable("id") Long id) {
        try {
            accountUserRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {}
    }
}
