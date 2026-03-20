package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.domain.Cart;
import com.lalke.bookstore.repositories.BookRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@SessionAttributes("cart")
@AllArgsConstructor
public class BookController {

    private final BookRepository bookRepository;

    @ModelAttribute("books")
    public List<Book> books() {
        return bookRepository.findAll();
    }

    @ModelAttribute("cart")
    public Cart cart() {
        return new Cart();
    }

    @GetMapping
    public String showCatalog() {
        return "purchaseView";
    }

    @GetMapping("/insert")
    public String showInsertForm(@ModelAttribute("book") Book book) {
        return "insertBookView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        model.addAttribute("book", book);
        return "insertBookView";
    }

    @PostMapping("/insert")
    public String processInsert(@ModelAttribute("book") Book book,
                                @RequestParam(value = "customAttributes.keys", required = false) List<String> keys,
                                @RequestParam(value = "customAttributes.values", required = false) List<String> values) {

        if (book.getId() != null && book.getId().trim().isEmpty()) {
            book.setId(null);
        }

        if (book.getCustomAttributes() != null) {
            book.getCustomAttributes().clear();
        }

        if (keys != null && values != null && keys.size() == values.size()) {
            for (int i = 0; i < keys.size(); i++) {
                if (!keys.get(i).isBlank()) {
                    book.addAttribute(keys.get(i), values.get(i));
                }
            }
        }
        bookRepository.save(book);
        return "redirect:/books";
    }

    @ModelAttribute("book")
    public Book book() {
        return new Book();
    }

    @GetMapping("/{id}")
    public String showBookDetails(@PathVariable("id") String id,
                                  Model model){
        Book selectedBook = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        model.addAttribute("selectedBook", selectedBook);
        return "bookDetailsView";
    }

    @GetMapping("/custom-attribute-row")
    public String getCustomAttributeRow(Model model){
        model.addAttribute("key", "");
        model.addAttribute("value", "");
        return "fragments/custom-attribute-row :: custom-attribute-row";
    }

    @GetMapping("/search")
    public String searchBooks(@RequestParam("title") String title, Model model) {
        List<Book> filteredBooks = bookRepository.findByTitleContainingIgnoreCase(title);
        model.addAttribute("books", filteredBooks);

        return "purchaseView :: #book-grid";
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id, HttpServletResponse response){
        bookRepository.deleteById(id);
        response.setHeader("HX-Redirect", "/books");
    }
}