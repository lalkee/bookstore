package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.services.CartService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("id") String id,
                            @ModelAttribute("cart") Cart cart,
                            Model model) {
        cartService.addToCart(id, cart);
        model.addAttribute("cart", cart);
        return "fragments/cart-fragments :: cart-count";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("id") String id,
                                 @ModelAttribute("cart") Cart cart,
                                 HtmxRequest htmxRequest) {
        cartService.removeFromCart(id, cart);
        if (htmxRequest.isHtmxRequest()) {
            return "cart/cartView :: main";
        }
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart(HtmxRequest htmxRequest, HttpServletResponse response) {
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Cart");
            return "cart/cartView :: main";
        }
        return "cart/cartView";
    }

}
