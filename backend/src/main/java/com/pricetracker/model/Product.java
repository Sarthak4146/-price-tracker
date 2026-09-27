package com.pricetracker.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Maps to the "products" table.
 *
 * Relationships:
 *   many Products  -> one User    (@ManyToOne below)
 *   one Product    -> many PriceHistory rows
 *   one Product    -> many Alerts
 *
 * This is the row created when the user pastes an Amazon/Flipkart link.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign key: which user is tracking this product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(nullable = false)
    private String name;

    private String imageUrl;

    // "AMAZON" or "FLIPKART" - detected from the URL
    @Column(nullable = false)
    private String platform;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    // The price the user wants to be alerted at (nullable - optional)
    @Column(name = "target_price")
    private BigDecimal targetPrice;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_checked_at")
    private LocalDateTime lastCheckedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
