package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.services.BookService;
import com.lalke.bookstore.services.ImageService;
import com.mongodb.client.gridfs.model.GridFSFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequestMapping("/books")
@SessionAttributes("cart")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ImageService imageService;

    @ModelAttribute("books")
    public List<Book> books() {
        return bookService.findAllBooks();
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping
    public String showCatalog() {
        return "homeView";
    }

    @GetMapping("/insert")
    public String showInsertForm(@ModelAttribute("book") Book book) {
        return "book/insertBookView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        return "book/insertBookView";
    }

    @PostMapping("/insert")
    public String processInsert(@ModelAttribute("book") Book book,
                                @RequestParam(value = "customAttributes.keys", required = false) List<String> keys,
                                @RequestParam(value = "customAttributes.values", required = false) List<String> values,
                                @RequestParam("file") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            String imageId = imageService.uploadImage(file);
            book.setCoverImageId(imageId);
        }

        bookService.saveBook(book, keys, values);

        return "redirect:/books";
    }

    @ModelAttribute("book")
    public Book book() {
        return new Book();
    }

    @GetMapping("/{id}")
    public String showBookDetails(@PathVariable("id") String id,
                                  Model model){
        Book selectedBook = bookService.findBookById(id);
        model.addAttribute("selectedBook", selectedBook);
        return "book/bookDetailsView";
    }

    @GetMapping("/custom-attribute-row")
    public String getCustomAttributeRow(Model model){
        model.addAttribute("key", "");
        model.addAttribute("value", "");
        return "fragments/custom-attribute-row :: custom-attribute-row";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam("title") String title, Model model) {
        List<Book> filteredBooks = bookService.findByTitleContainingIgnoreCase(title);
        model.addAttribute("books", filteredBooks);
        return "fragments/book-grid :: book-grid";
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id, HttpServletResponse response){
        bookService.deleteBookById(id);
        response.setHeader("HX-Redirect", "/books");
    }

    @GetMapping("/image/{id}")
    public void getCover(@PathVariable String id, HttpServletResponse response) throws IOException {
        imageService.renderImage(id, response);
    }
}