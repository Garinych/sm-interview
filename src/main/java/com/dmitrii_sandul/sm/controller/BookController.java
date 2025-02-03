package com.dmitrii_sandul.sm.controller;

import com.dmitrii_sandul.sm.model.Book;
import com.dmitrii_sandul.sm.model.BorrowRecord;
import com.dmitrii_sandul.sm.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Add book", description = "Allows a user to add a new book.")
    @PostMapping
    public Book addBook(@RequestParam String title, int count) {
        return bookService.addBook(title, count);
    }

    @Operation(summary = "Borrow a book", description = "Allows a user to borrow a book if available.")
    @PostMapping("/{bookId}/borrow")
    public BorrowRecord borrowBook(@PathVariable Long bookId, @RequestParam Long userId) {
        return bookService.borrowBook(bookId, userId);
    }

    @Operation(summary = "Return a book", description = "Allows a user to return a borrowed book.")
    @PostMapping("/{bookId}/return")
    @ResponseStatus(HttpStatus.OK)
    public void returnBook(@PathVariable Long bookId, @RequestParam Long userId) {
        bookService.returnBook(bookId, userId);
    }

    @Operation(summary = "Get all books borrowed by a user", description = "Retrieves all books borrowed by a specific user.")
    @GetMapping("/borrowed")
    public List<Book> getBooksBorrowedByUser(@RequestParam Long userId) {
        return bookService.getBooksBorrowedByUser(userId);
    }
}