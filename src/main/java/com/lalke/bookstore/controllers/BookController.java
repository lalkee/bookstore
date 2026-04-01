package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.services.BookService;
import com.lalke.bookstore.services.ImageService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public String showCatalog(Model model, HtmxRequest htmxRequest, HttpServletResponse response) {
        List<Book> featured = bookService.findRandomBooks(5);
        model.addAttribute("featuredBooks", featured);
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Catalog");
            return "homeView :: main";
        }
        return "homeView";
    }

    @GetMapping("/insert")
    public String showInsertForm(@ModelAttribute("book") Book book, HtmxRequest htmxRequest, HttpServletResponse response) {
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Add Book");
            return "book/insertBookView :: main";
        }
        return "book/insertBookView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model, HtmxRequest htmxRequest, HttpServletResponse response) {
        Book book = bookService.findBookById(id);
        model.addAttribute("book", book);
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Edit Book");
            return "book/insertBookView :: main";
        }
        return "book/insertBookView";
    }

    @PostMapping("/insert")
    public String processInsert(@ModelAttribute("book") Book book,
                                @RequestParam(value = "customAttributes.keys", required = false) List<String> keys,
                                @RequestParam(value = "customAttributes.values", required = false) List<String> values,
                                @RequestParam("file") MultipartFile file) throws IOException {

        bookService.saveBook(book, keys, values, file);

        return "redirect:/books";
    }

    @ModelAttribute("book")
    public Book book() {
        return new Book();
    }

    @GetMapping("/{id}")
    public String showBookDetails(@PathVariable("id") String id,
                                  Model model,
                                  HtmxRequest htmxRequest,
                                  HttpServletResponse response){
        Book selectedBook = bookService.findBookById(id);
        model.addAttribute("selectedBook", selectedBook);
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", selectedBook.getTitle());
            return "book/bookDetailsView :: main";
        }
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