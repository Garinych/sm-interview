package com.dmitrii_sandul.sm.repository;

import com.dmitrii_sandul.sm.model.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    boolean existsByBookIdAndUserProfileId(Long bookId, Long userId);

    Optional<BorrowRecord> findByBookIdAndUserProfileId(Long bookId, Long userId);

    List<BorrowRecord> findAllByUserProfileId(Long userId);
}
