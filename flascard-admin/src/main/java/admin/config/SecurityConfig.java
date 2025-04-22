package admin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import admin.entity.AccountUser;
import admin.repository.AccountUserRepository;

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
         http.csrf(csrf -> csrf.disable()).
         authorizeHttpRequests()
         .requestMatchers("/login").permitAll()
         .requestMatchers("/", "/**").hasRole("ADMIN")
         .and()
         .formLogin()
            .loginPage("/login")
                .defaultSuccessUrl("/")
        .and()
            .logout()
                .logoutSuccessUrl("/login");
    
        return http.build();
    }

}
