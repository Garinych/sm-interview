package com.dmitrii_sandul.sm.repository;

import com.dmitrii_sandul.sm.model.Book;
import com.dmitrii_sandul.sm.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
    Optional<BorrowRecord> findByBookIdAndUserId(Long bookId, Long userId);
}
