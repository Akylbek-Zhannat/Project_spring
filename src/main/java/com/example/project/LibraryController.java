package com.example.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
@Controller
@RequestMapping("/")
public class LibraryController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final FileStorageService fileStorageService;

    public LibraryController(BookService bookService, CategoryService categoryService, FileStorageService fileStorageService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/test")
    @ResponseBody
    public List<Book> testBooks() {
        return bookService.getAllBooks();
    }


    @GetMapping
    public String viewHomePage(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String categoryId,
            @RequestParam(value = "sort", required = false) String sort,
            Model model
    ) {
        List<Book> books = bookService.getAllBooks();

        // Фильтрация, поиск и сортировка
        if (search != null && !search.isEmpty()) {
            books = books.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        } else if (categoryId != null && !categoryId.isEmpty()) {
            books = books.stream()
                    .filter(book -> book.getCategory() != null && book.getCategory().getId().equals(categoryId))
                    .toList();
        }

        if ("price".equals(sort)) {
            books = books.stream().sorted(Comparator.comparing(Book::getPrice)).toList();
        } else if ("title".equals(sort)) {
            books = books.stream().sorted(Comparator.comparing(Book::getTitle)).toList();
        } else if ("author".equals(sort)) {
            books = books.stream().sorted(Comparator.comparing(Book::getAuthor)).toList();
        }

        // Добавляем книги и категории в модель
        model.addAttribute("books", books);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "index";
    }



    @GetMapping("/books/add")
    public String addBookPage(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-book";
    }


    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book, @RequestParam("image") MultipartFile file) {
        // Если пользователь загрузил файл, сохраняем его
        if (!file.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(file);
            book.setCoverImageUrl(imageUrl);
        }

        // Сохраняем книгу
        bookService.createBook(book);
        return "redirect:/";
    }




    @GetMapping("/books/edit/{id}")
    public String editBookPage(@PathVariable String id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit-book";
    }

    @PostMapping("/books/update/{id}")
    public String updateBook(@PathVariable String id, @ModelAttribute Book book, @RequestParam("image") MultipartFile file) {
        // Если пользователь загрузил новый файл, сохраняем его
        if (!file.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(file);
            book.setCoverImageUrl(imageUrl);
        } else {
            // Если файл не загружен, оставляем старую обложку
            Book existingBook = bookService.getBookById(id);
            book.setCoverImageUrl(existingBook.getCoverImageUrl());
        }

        bookService.updateBook(id, book);
        return "redirect:/";
    }


    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return "redirect:/";
    }
}