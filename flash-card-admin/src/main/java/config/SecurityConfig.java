package config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import entity.AccountUser;
import repository.AccountUserRepository;

@Configuration
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AccountUserRepository userRepository) {
        return username -> {
            AccountUser user = userRepository.findByUsername(username);
            if (user != null)
             return user;
            throw new UsernameNotFoundException("User "+ username + " not found");
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
        .authorizeRequests()
            .anyRequest().hasRole("ADMIN")
        .and()
            .formLogin().loginPage("/login")
        .and()
            .logout()
                .logoutSuccessUrl("/");
        return http.build();
    }
}
