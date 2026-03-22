package com.lalke.bookstore.services;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CartService {

    private final BookRepository bookRepository;

    public void addToCart(String id, Cart cart) {
        bookRepository.findById(id).ifPresent(cart::addItem);
    }

    public void removeFromCart(String id, Cart cart) {
        bookRepository.findById(id).ifPresent(cart::removeItem);
    }
}
