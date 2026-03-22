package com.lalke.bookstore.repositories;

import com.lalke.bookstore.domain.Author;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String> {
    List<Author> findByNameContainingIgnoreCase(String name);
}
