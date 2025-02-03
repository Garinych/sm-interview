package com.dmitrii_sandul.sm.repository;

import com.dmitrii_sandul.sm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
