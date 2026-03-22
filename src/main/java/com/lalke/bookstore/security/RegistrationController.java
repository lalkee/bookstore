package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;

    @GetMapping
    public String showRegisterForm() {
        return "auth/registerView";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        userService.registerNewUser(form);
        return "redirect:/";
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }
}
