package admin.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
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

import admin.dto.AccountUserDTO;
import admin.dto.CardDTO;
import admin.dto.CardSetDTO;
import admin.entity.AccountUser;
import admin.entity.CardSet;
import admin.repository.AccountUserRepository;
import admin.repository.CardSetRepository;

@RestController
@RequestMapping(path = "/api/account_user", produces = "application/json")
@CrossOrigin(origins= "${app.cors.origins}")
// @CrossOrigin(origins= "http://localhost:8080")
public class APIAccountUserController {
    private AccountUserRepository accountUserRepository;
    private CardSetRepository cardSetRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public APIAccountUserController(AccountUserRepository accountUserRepository, CardSetRepository cardSetRepository, PasswordEncoder passwordEncoder) {
        this.accountUserRepository = accountUserRepository;
        this.cardSetRepository = cardSetRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/get_all")
    public Iterable<AccountUserDTO> allAccountUsers() {
        List<AccountUser> accountUsers= (List<AccountUser>)accountUserRepository.findAll();
        List<AccountUserDTO> accIterable = new ArrayList<>();
        for (AccountUser accountUser : accountUsers) {
            accIterable.add(
                accountUser.createDTO()
            );
        } 
        return (Iterable<AccountUserDTO>) accIterable;
    }
    @GetMapping("/getById/{id}")
    public ResponseEntity<AccountUserDTO> accountUserById(@PathVariable("id") Long id) {
        Optional<AccountUser> optAccountUser = accountUserRepository.findById(id);

        if(optAccountUser.isPresent()){
            return new ResponseEntity<>(optAccountUser.get().createDTO(), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    @GetMapping("/getByUsername/{username}")
    public Iterable<AccountUserDTO> accountUserByUserName(@PathVariable("username") String username) {
        var users = (List<AccountUserDTO>) allAccountUsers();
        users.removeIf(user->!user.getUsername().contains(username));
        return (Iterable<AccountUserDTO>) users;
    }
    @PostMapping(path="/create", consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountUser postAccountUser(@RequestBody AccountUser accountUser) {
        return accountUserRepository.save(new AccountUser(
            accountUser.getUsername(), passwordEncoder.encode(accountUser.getPassword()), accountUser.getAuthority()) 
        );
    }
    @PostMapping(path="/create_card_set/{id}", consumes ="application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountUserDTO addCardSettoAccountUser(@PathVariable Long id, @RequestBody CardSetDTO cardSetDTO) {
        AccountUser accountUser = accountUserRepository.findById(id).get();
        CardSet newCardSet = new CardSet();
        newCardSet.setName(cardSetDTO.getName());

        newCardSet.setAccountUser(accountUser);
        accountUser.getCard_sets().add(newCardSet);
        return accountUserRepository.save(accountUser).createDTO();
    }
    @PatchMapping(path="/update/{id}", consumes = "application/json")
    public AccountUserDTO putAccountUser(@PathVariable("id") Long id, @RequestBody AccountUserDTO patch) {
        patch.setPassword(passwordEncoder.encode(patch.getPassword()));
        AccountUser accountUser = accountUserRepository.findById(id).get();
        if (patch.getUsername() != null)
          accountUser.setUsername(patch.getUsername());
        if (patch.getPassword() != null)
            accountUser.setPassword(patch.getPassword());
        if (patch.getAuthority() != null)
            accountUser.setAuthority(patch.getAuthority());
        return accountUserRepository.save(accountUser).createDTO();
    }
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountUser(@PathVariable("id") Long id) {
         try {
            AccountUserDTO user = accountUserById(id).getBody();
            user.delete();
            accountUserRepository.deleteById(id);
        } catch(EmptyResultDataAccessException e) {}
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> login (@RequestBody AccountUserDTO loginRequest) {

        Optional<AccountUser> optionalAccountUser = accountUserRepository.findByUsername(loginRequest.getUsername());
        Map<String,String> response = new HashMap<>();
        if (optionalAccountUser.isPresent() 
        && passwordEncoder.matches(loginRequest.getPassword(), optionalAccountUser.get().getPassword())
        && optionalAccountUser.get().getAuthority().equals("ROLE_USER"))
        {
            response.put("message", "Login successfully");
            return ResponseEntity.ok(response);
        }
        response.put("message","Invalid username or password");
            
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
}
