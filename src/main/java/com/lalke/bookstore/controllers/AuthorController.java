package com.lalke.bookstore.controllers;

import com.lalke.bookstore.domain.Author;
import com.lalke.bookstore.domain.Book;
import com.lalke.bookstore.services.AuthorService;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/authors")
@AllArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @ModelAttribute("author")
    public Author author() {
        return new Author();
    }

    @GetMapping
    public String showAuthors(Model model, HtmxRequest htmxRequest, HttpServletResponse response) {
        List<Author> authors = authorService.findAllAuthors();
        Map<String, Long> bookCounts = authorService.getBookCounts(authors);

        model.addAttribute("authors", authors);
        model.addAttribute("bookCounts", bookCounts);
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Authors");
            return "author/authorsView :: main";
        }
        return "author/authorsView";
    }

    @GetMapping("/{id}")
    public String showAuthorDetails(@PathVariable("id") String id, Model model, HtmxRequest htmxRequest, HttpServletResponse response) {
        Author selectedAuthor = authorService.findAuthorById(id);
        List<Book> authorBooks = authorService.findBooksByAuthorId(id);

        model.addAttribute("selectedAuthor", selectedAuthor);
        model.addAttribute("authorBooks", authorBooks);
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", selectedAuthor.getName());
            return "author/authorDetailsView :: main";
        }
        return "author/authorDetailsView";
    }

    @GetMapping("/insert")
    public String showInsertForm(HtmxRequest htmxRequest, HttpServletResponse response) {
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Add Author");
            return "author/insertAuthorView :: main";
        }
        return "author/insertAuthorView";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") String id, Model model, HtmxRequest htmxRequest, HttpServletResponse response) {
        Author author = authorService.findAuthorById(id);
        model.addAttribute("author", author);
        if (htmxRequest.isHtmxRequest()) {
            response.setHeader("HX-Title", "Edit Author");
            return "author/insertAuthorView :: main";
        }
        return "author/insertAuthorView";
    }

    @PostMapping("/insert")
    public String processInsert(@ModelAttribute("author") Author author,
                                @RequestParam(value = "customAttributes.keys", required = false) List<String> keys,
                                @RequestParam(value = "customAttributes.values", required = false) List<String> values,
                                @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        authorService.saveAuthor(author, keys, values, file);
        return "redirect:/authors";
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable String id, HttpServletResponse response) {
        try {
            authorService.deleteAuthorById(id);
            response.setHeader("HX-Redirect", "/authors");
        } catch (IllegalStateException e) {
            response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
            response.setHeader("HX-Trigger", "{\"showMessage\": \"" + e.getMessage() + "\"}");
        }
    }

    @GetMapping("/search")
    public String searchAuthors(@RequestParam(value = "authorName", required = false) String authorName, Model model) {
        List<Author> authors;

        if (authorName == null || authorName.isBlank()) {
            authors = authorService.findAllAuthors();
        } else {
            authors = authorService.findAuthorsByName(authorName);
        }

        Map<String, Long> bookCounts = authorService.getBookCounts(authors);

        model.addAttribute("authors", authors);
        model.addAttribute("bookCounts", bookCounts);

        return "fragments/author-grid :: author-grid";
    }

    @GetMapping("/search-suggestions")
    public String getSearchSuggestions(@RequestParam("authorName") String name, Model model) {
        if (name == null || name.trim().length() < 2) {
            return "fragments/author-suggestions :: suggestions";
        }

        List<Author> suggestions = authorService.findAuthorsByName(name);
        model.addAttribute("suggestedAuthors", suggestions);

        return "fragments/author-suggestions :: suggestions";
    }
}