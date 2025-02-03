package com.dmitrii_sandul.sm.repository;

import com.dmitrii_sandul.sm.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}