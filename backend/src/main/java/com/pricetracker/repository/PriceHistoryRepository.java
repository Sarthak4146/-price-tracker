package com.pricetracker.repository;

import com.pricetracker.model.PriceHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {

    // Full history for a product, oldest first - used to draw the trend chart
    List<PriceHistory> findByProductIdOrderByCheckedAtAsc(Long productId);

    // Pagination + sorting example (Pageable is Spring Data's built-in paging tool)
    List<PriceHistory> findByProductIdOrderByCheckedAtDesc(Long productId, Pageable pageable);

    // JPQL aggregate queries -> power the "Price Analysis" feature (min/max/avg)
    @Query("SELECT MIN(ph.price) FROM PriceHistory ph WHERE ph.product.id = :productId")
    BigDecimal findMinPrice(@Param("productId") Long productId);

    @Query("SELECT MAX(ph.price) FROM PriceHistory ph WHERE ph.product.id = :productId")
    BigDecimal findMaxPrice(@Param("productId") Long productId);

    @Query("SELECT AVG(ph.price) FROM PriceHistory ph WHERE ph.product.id = :productId")
    Double findAvgPrice(@Param("productId") Long productId);

    @Query("SELECT COUNT(ph) FROM PriceHistory ph WHERE ph.product.id = :productId")
    long countByProductId(@Param("productId") Long productId);
}
