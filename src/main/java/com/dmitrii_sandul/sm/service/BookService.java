package com.dmitrii_sandul.sm.service;

import com.dmitrii_sandul.sm.exception.*;
import com.dmitrii_sandul.sm.model.Book;
import com.dmitrii_sandul.sm.model.BorrowRecord;
import com.dmitrii_sandul.sm.model.UserProfile;
import com.dmitrii_sandul.sm.repository.BookRepository;
import com.dmitrii_sandul.sm.repository.BorrowRecordRepository;
import com.dmitrii_sandul.sm.repository.UserRepository;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private static final int MAX_RETRY_ATTEMPTS = 3;

    public BookService(UserRepository userRepository, BookRepository bookRepository, BorrowRecordRepository borrowRecordRepository) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.borrowRecordRepository = borrowRecordRepository;
    }

    public Book addBook(String title, int count) {
        Book newBook = new Book();
        newBook.setTitle(title);
        newBook.setCount(count);
        return bookRepository.save(newBook);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public BorrowRecord borrowBook(Long bookId, Long userId) {
        for (int attempt = 0; attempt < MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                UserProfile userProfile = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException("User not found"));
                Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new BookNotFoundException("Book not found"));

                if (book.getCount() <= 0) {
                    throw new NoCopiesAvailableException("No copies available");
                }

                if (borrowRecordRepository.existsByBookIdAndUserProfileId(bookId, userId)) {
                    throw new BookAlreadyBorrowedException("User has already borrowed this book");
                }

                book.setCount(book.getCount() - 1);
                bookRepository.save(book);

                BorrowRecord borrowRecord = new BorrowRecord();
                borrowRecord.setBook(book);
                borrowRecord.setUserProfile(userProfile);
                borrowRecord.setBorrowDate(LocalDateTime.now());
                return borrowRecordRepository.save(borrowRecord);
            } catch (ObjectOptimisticLockingFailureException e) {
                if (attempt == MAX_RETRY_ATTEMPTS - 1) {
                    throw new NoCopiesAvailableException("The book is already borrowed by another user. Please try again.");
                }
                // a small delay before retrying
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new RetryException("Failed to borrow the book after several attempts. Please try again.");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void returnBook(Long bookId, Long userId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findByBookIdAndUserProfileId(bookId, userId)
                .orElseThrow(() -> new RuntimeException("No borrow record found for this user and book."));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setCount(book.getCount() + 1);
        bookRepository.save(book);

        borrowRecordRepository.delete(borrowRecord);
    }

    public List<Book> getBooksBorrowedByUser(Long userId) {
        return borrowRecordRepository.findAllByUserProfileId(userId).stream()
                .map(BorrowRecord::getBook)
                .collect(Collectors.toList());
    }
}