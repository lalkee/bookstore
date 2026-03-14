package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.repositories.BookRepository;
import com.lalke.bookstore.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cart")
@AllArgsConstructor
public class CartController {

    private final BookRepository bookRepository;
    private final CartRepository cartRepository;

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("id") String id,
                            @ModelAttribute("cart") Cart cart) {
        bookRepository.findById(id).ifPresent(cart::addItem);
        return "redirect:/books";
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

//    @PostMapping("/purchase")
//    public String purchase(@RequestParam String address,
//                           @ModelAttribute("cart") Cart cart) {
//        cart.setAddress(address);
//        cartRepository.save(cart);
//        cart.getItems().clear();
//        return "redirect:/books";
//    }
}
