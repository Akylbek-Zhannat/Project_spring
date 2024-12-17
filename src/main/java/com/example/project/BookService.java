package com.example.project;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(String id);
    Book createBook(Book book);
    Book updateBook(String id, Book book);
    void deleteBook(String id);
    boolean isBookInFavorites(String username, String bookId);
    void addBookToFavorites(String username, String bookId);
    void removeBookFromFavorites(String username, String bookId);
    void reserveBook(String bookId, String reserveDateTime);

}
