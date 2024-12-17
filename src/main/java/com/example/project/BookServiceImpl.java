package com.example.project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(String id, Book book) {
        book.setId(id); // Установите ID для обновления
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }



    @Override
    public void reserveBook(String bookId, String reserveDateTime) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setReservedDate(reserveDateTime);
            bookRepository.save(book);
            System.out.println("Book reserved for date and time: " + reserveDateTime);
        } else {
            throw new RuntimeException("Book not found");
        }
    }

    @Override
    public boolean isBookInFavorites(String username, String bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getFavorites().contains(bookId);
    }

    @Override
    public void addBookToFavorites(String username, String bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getFavorites().contains(bookId)) {
            user.getFavorites().add(bookId);
            userRepository.save(user);
        }
    }

    @Override
    public void removeBookFromFavorites(String username, String bookId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.getFavorites().remove(bookId);
        userRepository.save(user);
    }

}
