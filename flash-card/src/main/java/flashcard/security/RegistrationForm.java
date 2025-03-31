package flashcard.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import flashcard.AccountUser;
import lombok.Data;

@Data
public class RegistrationForm {

  private String username;
  private String password;
  private String fullname;
  private String phone;
  
  public AccountUser toUser(PasswordEncoder passwordEncoder) {
    return new AccountUser(
        username, passwordEncoder.encode(password), 
        fullname, phone);
  }
  
}
