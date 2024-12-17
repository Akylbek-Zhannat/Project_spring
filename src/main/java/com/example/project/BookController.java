package com.example.project;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final EmailService emailService;

    public BookController(BookService bookService, EmailService emailService) {
        this.bookService = bookService;
        this.emailService = emailService;
    }

    @GetMapping("/{id}")
    public String viewBookDetails(@PathVariable("id") String id, Model model, Principal principal) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        String username = principal.getName();
        boolean isFavorite = bookService.isBookInFavorites(username, id);
        model.addAttribute("book", book);
        model.addAttribute("isFavorite", isFavorite);
        return "book-details";
    }

    @PostMapping("/favorites/toggle/{id}")
    public String toggleFavorite(@PathVariable("id") String id, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        if (bookService.isBookInFavorites(username, id)) {
            bookService.removeBookFromFavorites(username, id);
            redirectAttributes.addFlashAttribute("message", "Book removed from favorites.");
        } else {
            bookService.addBookToFavorites(username, id);
            redirectAttributes.addFlashAttribute("message", "Book added to favorites!");
        }
        return "redirect:/books/{id}";
    }


    @PostMapping("/reserve/{id}")
    public String reserveBook(@PathVariable("id") String id,
                              @RequestParam("reserveDateTime") String reserveDateTime,
                              RedirectAttributes redirectAttributes) {
        try {
            bookService.reserveBook(id, reserveDateTime);
            Book book = bookService.getBookById(id);
            String currentUserEmail = "akylbekzhannat65@gmail.com";
            emailService.sendBookingConfirmation(currentUserEmail, book.getTitle(), reserveDateTime);
            redirectAttributes.addFlashAttribute("message",
                    "Book reserved successfully for " + reserveDateTime + " and confirmation email sent!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to reserve book: " + e.getMessage());
        }
        return "redirect:/books/{id}";
    }

}
