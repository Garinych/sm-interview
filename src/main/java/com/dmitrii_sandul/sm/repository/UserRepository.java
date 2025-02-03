package com.dmitrii_sandul.sm.repository;

import com.dmitrii_sandul.sm.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, Long> {
}
