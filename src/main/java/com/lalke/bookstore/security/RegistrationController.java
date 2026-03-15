package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.repositories.UserRepository;
import jakarta.servlet.Registration;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
@RequestMapping("/register")
public class RegistrationController {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showRegisterForm() {
        return "registerView";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        userRepository.save(form.toUser(passwordEncoder));
        return "redirect:/";
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }
}
