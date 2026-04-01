package com.lalke.bookstore.security;

import com.lalke.bookstore.domain.Cart;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@AllArgsConstructor
@SessionAttributes("cart")
@RequestMapping("/login")
public class LoginController {

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping()
    public String login(HtmxRequest htmxRequest, HttpServletResponse response) {
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Login - Bookstore");
            return "auth/loginView :: main";
        }
        return "auth/loginView";
    }
}
