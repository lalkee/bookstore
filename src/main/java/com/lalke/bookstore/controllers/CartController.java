package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
@AllArgsConstructor
public class CartController {

    private final BookRepository bookRepository;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("id") String id,
                            @ModelAttribute("cart") Cart cart,
                            Model model) {
        bookRepository.findById(id).ifPresent(cart::addItem);
        model.addAttribute("cart", cart);
        return "fragments/cart-fragments :: cart-count";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("id") String id,
                                 @ModelAttribute("cart") Cart cart) {
        bookRepository.findById(id).ifPresent(cart::removeItem);
        return "redirect:/cart";
    }

    @GetMapping
    public String showCart() {
        return "cartView";
    }

}
