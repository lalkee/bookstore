package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Author;
import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.repositories.AuthorRepository;
import com.lalke.bookstore.repositories.BookRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @ModelAttribute("author")
    public Author author() {
        return new Author();
    }

    @GetMapping
    public String showAuthors(Model model) {
        List<Author> authors = authorRepository.findAll();
        Map<String, Long> bookCounts = authors.stream()
                .collect(Collectors.toMap(
                        Author::getId,
                        a -> bookRepository.countByAuthorId(a.getId())
                ));

        model.addAttribute("authors", authors);
        model.addAttribute("bookCounts", bookCounts);
        return "authorsView";
    }

    @GetMapping("/{id}")
    public String showAuthorDetails(@PathVariable("id") String id, Model model) {
        Author selectedAuthor = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author Id:" + id));

        List<Book> authorBooks = bookRepository.findByAuthorId(id);

        model.addAttribute("selectedAuthor", selectedAuthor);
        model.addAttribute("authorBooks", authorBooks);
        return "authorDetailsView";
    }

    @GetMapping("/insert")
    public String showInsertForm() {
        return "insertAuthorView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid author Id:" + id));
        model.addAttribute("author", author);
        return "insertAuthorView";
    }

    @PostMapping("/insert")
    public String processInsert(@ModelAttribute("author") Author author,
                                @RequestParam(value = "customAttributes.keys", required = false) List<String> keys,
                                @RequestParam(value = "customAttributes.values", required = false) List<String> values) {

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
        return "redirect:/authors";
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable String id, HttpServletResponse response) {
        authorRepository.deleteById(id);
        response.setHeader("HX-Redirect", "/authors");
    }

    @GetMapping("/search-suggestions")
    public String getSuggestions(@RequestParam(value = "authorName", required = false) String name, Model model) {
        if (name == null || name.isBlank()) {
            return "fragments/author-suggestions :: suggestions";
        }

        List<Author> suggestions = authorRepository.findByNameContainingIgnoreCase(name);
        model.addAttribute("suggestedAuthors", suggestions);
        return "fragments/author-suggestions :: suggestions";
    }
}