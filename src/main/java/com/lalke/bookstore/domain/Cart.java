package com.lalke.bookstore.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Book book) {
        items.stream()
                .filter(i -> i.getBookId().equals(book.getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> items.add(new CartItem(book.getId(), book.getTitle(), book.getPrice(), 1))
                );
    }

    public void removeItem(Book book) {
        items.removeIf(i -> i.getBookId().equals(book.getId()));
    }

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP); // Ensure 2 decimal places
    }
}
