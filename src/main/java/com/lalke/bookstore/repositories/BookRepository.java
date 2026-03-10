package com.lalke.bookstore.repositories;

import com.lalke.bookstore.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookRepository extends MongoRepository<Book, String> {
}
