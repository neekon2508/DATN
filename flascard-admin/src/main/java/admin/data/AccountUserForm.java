package admin.data;
import org.springframework.security.crypto.password.PasswordEncoder;

import admin.entity.AccountUser;
import lombok.Data;

@Data
public class AccountUserForm {

    private String username;
    private String password;

    public AccountUser toUser(PasswordEncoder passwordEncoder) {
        return new AccountUser(username, passwordEncoder.encode(password), "ROLE_USER");
    }
}
