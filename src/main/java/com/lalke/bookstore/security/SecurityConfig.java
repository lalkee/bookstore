package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.User;
import com.lalke.bookstore.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            System.out.println("DEBUG: Attempting login for username: " + username);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> {
                        System.out.println("DEBUG: User NOT found in MongoDB: " + username);
                        return new UsernameNotFoundException("User not found");
                    });

            System.out.println("DEBUG: User found!");
            System.out.println("DEBUG: Encoded password in DB: " + user.getPassword());
            System.out.println("DEBUG: User Roles: " + user.getAuthorities());

            return user;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/insert", "/orders").hasRole("ADMIN")
                        .requestMatchers("/", "/**").permitAll()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .permitAll()
                )
                .build();
    }
}
