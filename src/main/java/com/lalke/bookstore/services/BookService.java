package com.lalke.bookstore.services;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.repositories.AuthorRepository;
import com.lalke.bookstore.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public Book findBookById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));
    }

    public void saveBook(Book book, List<String> keys, List<String> values) {
        if (book.getId() != null && book.getId().trim().isEmpty()) {
            book.setId(null);
        }

        if (book.getAuthorId() == null || book.getAuthorId().isBlank()) {
            com.lalke.bookstore.domain.Author newAuthor = com.lalke.bookstore.domain.Author.builder()
                    .name(book.getAuthorName())
                    .build();
            authorRepository.save(newAuthor);
            book.setAuthorId(newAuthor.getId());
        }

        if (book.getCustomAttributes() != null) {
            book.getCustomAttributes().clear();
        }

        if (keys != null && values != null && keys.size() == values.size()) {
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) != null && !keys.get(i).isBlank()) {
                    book.addAttribute(keys.get(i), values.get(i));
                }
            }
        }

        bookRepository.save(book);
    }

    public List<Book> findByTitleContainingIgnoreCase(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public void deleteBookById(String id) {
        bookRepository.deleteById(id);
    }
}
