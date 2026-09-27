package com.pricetracker.repository;

import com.pricetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * JpaRepository<User, Long> already gives us save(), findById(), findAll(),
 * deleteById() etc for free. Below are "query methods" - Spring Data JPA
 * reads the method name and generates the SQL automatically, no code needed.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
