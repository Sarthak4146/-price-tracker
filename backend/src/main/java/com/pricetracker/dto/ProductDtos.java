package com.pricetracker.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductDtos {

    /** What the frontend sends when the user pastes a URL to track. */
    @Data
    public static class AddProductRequest {
        @NotBlank
        private String url;

        // Optional: alert when price drops to/below this
        private BigDecimal targetPrice;
    }

    @Data
    public static class UpdateTargetPriceRequest {
        private BigDecimal targetPrice;
    }

    /** What we send back to the frontend for each tracked product. */
    @Data
    public static class ProductResponse {
        private Long id;
        private String url;
        private String name;
        private String imageUrl;
        private String platform;
        private BigDecimal currentPrice;
        private BigDecimal targetPrice;
        private boolean active;
        private LocalDateTime createdAt;
        private LocalDateTime lastCheckedAt;

        // Extra computed fields for the dashboard/product list
        private BigDecimal minPrice;
        private BigDecimal maxPrice;
        private Double avgPrice;
    }

    @Data
    public static class PriceHistoryPoint {
        private BigDecimal price;
        private LocalDateTime checkedAt;

        public PriceHistoryPoint(BigDecimal price, LocalDateTime checkedAt) {
            this.price = price;
            this.checkedAt = checkedAt;
        }
    }
}
