package com.lalke.bookstore.services;

import com.lalke.bookstore.domain.User;
import com.lalke.bookstore.repositories.UserRepository;
import com.lalke.bookstore.security.RegistrationForm;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
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
    }

    public void registerNewUser(RegistrationForm form) {
        userRepository.save(form.toUser(passwordEncoder));
    }
}
