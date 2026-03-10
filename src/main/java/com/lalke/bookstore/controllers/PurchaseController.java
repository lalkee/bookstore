package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.repositories.BookRepository;
import com.lalke.bookstore.repositories.CartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@SessionAttributes("cart")
@AllArgsConstructor
public class PurchaseController {

    private BookRepository bookRepository;
    private CartRepository cartRepository;

    @ModelAttribute(name= "books")
    public List<Book> books() {
        return bookRepository.findAll();
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @ModelAttribute("book")
    public Book book() {
        return new Book();
    }

    @GetMapping
    public String showPurchaseForm() {
        return "purchaseView";
    }

    @PostMapping
    public String addToCart(@RequestParam("id") String id,
                            @ModelAttribute Cart cart) {
        bookRepository.findById(id).ifPresent(book -> {
            cart.addBook(book);
            log.info("Added to cart: {}", book);
        });

        return "purchaseView";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam("id") String id,
                                 @ModelAttribute("cart") Cart cart) {
        log.info("Removed book with id {} from cart.",
                cart.getBooks().removeIf(book -> book.getId().equals(id)));

        return "cartView";
    }

    @GetMapping("/cart")
    public String showCartPage() {
        return "cartView";
    }

    @PostMapping("/purchase")
    public String processOrder(@ModelAttribute("cart") Cart cart,
                               @RequestParam("address") String address) {
        log.info("Processing order for address: {} with {} items", address, cart.getBooks().size());

        cart.setAddress("Order for: " + address);
        cartRepository.save(cart);

        cart.getBooks().clear();
        return "redirect:/";
    }

    @GetMapping("/insert")
    public String showInsertForm() {
        return "insertBookView";
    }

    @PostMapping("/insert")
    public String processInsert(@ModelAttribute("book") Book book) {
        log.info("Saving new book: {}", book);
        bookRepository.save(book);
        return "redirect:/";
    }
}