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
            bookRepository.save(Book.builder()
                    .id("1")
                    .title("Dan sesti")
                    .price(new BigDecimal("600"))
                    .build());

            bookRepository.save(Book.builder()
                    .id("2")
                    .title("Knjiga o Blamu")
                    .price(new BigDecimal("500"))
                    .build());

            bookRepository.save(Book.builder()
                    .id("3")
                    .title("Pescanik")
                    .price(new BigDecimal("450"))
                    .build());

            log.info("Books initialized in MongoDB.");
        };
    }
}