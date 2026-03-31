package com.lalke.bookstore.repositories;

import com.lalke.bookstore.domain.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByAuthorId(String id);

    Long countByAuthorId(String id);

    boolean existsByAuthorId(String id);
}
