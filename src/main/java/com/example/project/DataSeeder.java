package com.example.project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Component
//public class DataSeeder implements CommandLineRunner {
//
//    private final CategoryRepository categoryRepository;
//    private final BookRepository bookRepository;
//
//    public DataSeeder(CategoryRepository categoryRepository, BookRepository bookRepository) {
//        this.categoryRepository = categoryRepository;
//        this.bookRepository = bookRepository;
//    }

//    @Override
//    public void run(String... args) throws Exception {
//        // Удаляем старые данные
//        categoryRepository.deleteAll();
//        bookRepository.deleteAll();
//
//        // Создаем категории
//        Category fiction = new Category();
//        fiction.setName("Fiction");
//        fiction.setDescription("Fictional books");
//        categoryRepository.save(fiction);
//
//        Category science = new Category();
//        science.setName("Science");
//        science.setDescription("Books about science");
//        categoryRepository.save(science);
//
//        // Создаем книги
//        Book book1 = new Book();
//        book1.setTitle("1984");
//        book1.setAuthor("George Orwell");
//        book1.setIsbn("9780451524935");
//        book1.setPrice(9.99);
//        book1.setCoverImageUrl("https://example.com/1984.jpg");
//        book1.setCategory(fiction);
//        bookRepository.save(book1);
//
//        Book book2 = new Book();
//        book2.setTitle("A Brief History of Time");
//        book2.setAuthor("Stephen Hawking");
//        book2.setIsbn("9780553380163");
//        book2.setPrice(14.99);
//        book2.setCoverImageUrl("https://example.com/brief-history.jpg");
//        book2.setCategory(science);
//        bookRepository.save(book2);
//    }
//}