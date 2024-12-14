package com.example.project;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(String id);
    Book createBook(Book book);
    Book updateBook(String id, Book book);
    void deleteBook(String id);
}
