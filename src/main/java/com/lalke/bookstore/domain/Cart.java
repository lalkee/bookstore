package com.lalke.bookstore.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;
    private String address;
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
    }
}
