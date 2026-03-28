package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.domain.User;
import com.lalke.bookstore.dto.UserDto;
import com.lalke.bookstore.security.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
@RequestMapping("/register")
public class RegistrationController {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final MessageSource messages;

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/registerView";
    }

    @PostMapping
    public String processRegistration(@ModelAttribute("user") @Valid UserDto userDto, Errors errors, HttpServletRequest request) {

        if (errors.hasErrors()) {
            return "auth/registerView :: registerContent";
        }

        User registered = null;

        try {
            registered = userService.registerNewUserAccount(userDto);

            String token = UUID.randomUUID().toString();
            userService.createVerificationToken(registered, token);

            String baseUrl = ServletUriComponentsBuilder.fromRequest(request)
                    .replacePath(request.getContextPath())
                    .build()
                    .toUriString();

            String confirmationUrl = baseUrl + "/register/registrationConfirm?token=" + token;

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(registered.getEmail());
            email.setSubject("Registration Confirmation");
            email.setText("Confirm here: " + confirmationUrl);

            mailSender.send(email);

            return "fragments/registerSuccess";

        } catch (Exception ex) {
            if (registered != null) {
                userService.deleteUser(registered);
            }

            if (ex.getMessage() != null && ex.getMessage().contains("already exists")) {
                errors.rejectValue("email", "message.regError", "Email already exists.");
            } else {
                errors.reject("globalError", "Mail server error. Registration cancelled. Please try again.");
            }

            return "auth/registerView :: registerContent";
        }
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token) {
        boolean result = userService.validateVerificationToken(token);
        if (result) {
            return "redirect:/login?verified=true";
        }
        return "redirect:/register?error=invalid";
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }
}