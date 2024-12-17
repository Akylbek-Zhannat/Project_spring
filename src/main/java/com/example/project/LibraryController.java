package com.example.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;

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


    @GetMapping
    public String viewHomePage(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "category", required = false) String categoryId,
            @RequestParam(value = "sort", required = false) String sort,
            Model model
    ) {
        List<Book> books = bookService.getAllBooks();
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
        if (!file.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(file);
            book.setCoverImageUrl(imageUrl);
        }
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
        if (!file.isEmpty()) {
            String imageUrl = fileStorageService.storeFile(file);
            book.setCoverImageUrl(imageUrl);
        } else {
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