package com.lalke.bookstore.services;

import com.lalke.bookstore.domain.Author;
import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.repositories.AuthorRepository;
import com.lalke.bookstore.repositories.BookRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

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
            Author newAuthor = Author.builder()
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

    public InputStream getResource(String fileId) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (file != null) {
            return gridFsOperations.getResource(file).getInputStream();
        }
        return null;
    }

    public List<Book> findByTitleContainingIgnoreCase(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public void deleteBookById(String id) {
        bookRepository.deleteById(id);
    }
}