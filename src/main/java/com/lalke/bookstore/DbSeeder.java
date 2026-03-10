package com.lalke.bookstore;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class DbSeeder {

    @Bean
    public ApplicationRunner seedDb(BookRepository bookRepository) {
        return args -> {
            bookRepository.save(new Book("1", "Dan sesti", 600));
            bookRepository.save(new Book("2", "Knjiga o Blamu", 450));
            bookRepository.save(new Book("3", "Pescanik", 500));
            log.info("Books initialized in MongoDB.");
        };
    }
}
