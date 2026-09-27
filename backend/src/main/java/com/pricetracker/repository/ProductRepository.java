package com.pricetracker.repository;

import com.pricetracker.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Query method: SELECT * FROM products WHERE user_id = ? ORDER BY created_at DESC
    List<Product> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Query method with LIKE - used by the Search & Filter feature
    List<Product> findByUserIdAndNameContainingIgnoreCase(Long userId, String name);

    // All active products - used by the scheduler to know what to re-check
    List<Product> findByActiveTrue();

    // Example JPQL (@Query) - fetch a single product only if it belongs to that user,
    // so one user can never read/alter another user's tracked product.
    @Query("SELECT p FROM Product p WHERE p.id = :id AND p.user.id = :userId")
    Product findByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    long countByUserId(Long userId);
}
