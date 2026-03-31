package com.lalke.bookstore.services;

import com.lalke.bookstore.domain.Author;
import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.repositories.AuthorRepository;
import com.lalke.bookstore.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ImageService imageService;

    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    public Map<String, Long> getBookCounts(List<Author> authors) {
        return authors.stream()
                .collect(Collectors.toMap(
                        Author::getId,
                        a -> bookRepository.countByAuthorId(a.getId())
                ));
    }

    public Author findAuthorById(String id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author Id:" + id));
    }

    public List<Book> findBooksByAuthorId(String id) {
        return bookRepository.findByAuthorId(id);
    }

    public void saveAuthor(Author author, List<String> keys, List<String> values, MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            String imageId = imageService.uploadImage(file);
            author.setProfileImageId(imageId);
        }

        if (author.getId() != null && author.getId().trim().isEmpty()) {
            author.setId(null);
        }

        if (author.getCustomAttributes() != null) {
            author.getCustomAttributes().clear();
        }

        if (keys != null && values != null && keys.size() == values.size()) {
            for (int i = 0; i < keys.size(); i++) {
                if (keys.get(i) != null && !keys.get(i).isBlank()) {
                    author.addAttribute(keys.get(i), values.get(i));
                }
            }
        }
        authorRepository.save(author);
    }

    public void deleteAuthorById(String id) {
        if(bookRepository.existsByAuthorId(id))
            throw new IllegalStateException("Cannot delete author: They are still linked to existing books.");
        else
        authorRepository.deleteById(id);
    }

    public List<Author> findAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
}