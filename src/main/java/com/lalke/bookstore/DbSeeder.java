package com.lalke.bookstore;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Slf4j
@Configuration
public class DbSeeder {

    @Bean
    public ApplicationRunner seedDb(BookRepository bookRepository) {
        return args -> {
            bookRepository.save(new Book("1", "Dan sesti", new BigDecimal("600")));
            bookRepository.save(new Book("2", "Knjiga o Blamu", new BigDecimal("500")));
            bookRepository.save(new Book("3", "Pescanik", new BigDecimal("450")));
            log.info("Books initialized in MongoDB.");
        };
    }
}
