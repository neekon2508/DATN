package admin.config;


import javax.swing.Spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.Conflict;
import org.springframework.web.client.RestTemplate;

import admin.entity.AccountUser;
import admin.repository.AccountUserRepository;
import admin.service.AccountUserService;
import admin.service.RestAccountUserService;

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
         .requestMatchers("/").hasRole("ADMIN")
         .anyRequest().permitAll()
         .and()
         .formLogin()
            .loginPage("/login")
                .defaultSuccessUrl("/")
        .and()
            .logout()
                .logoutSuccessUrl("/login");
    
        return http.build();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // @Bean
    // public AccountUserService accountUserService() {
    //     return new RestAccountUserService(new RestTemplate());
    // }

}
