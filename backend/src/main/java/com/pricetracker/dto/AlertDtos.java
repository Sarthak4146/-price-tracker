package com.pricetracker.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AlertDtos {

    @Data
    public static class AlertResponse {
        private Long id;
        private Long productId;
        private String productName;
        private String message;
        private BigDecimal triggeredPrice;
        private boolean read;
        private LocalDateTime createdAt;
    }

    /** Powers the Dashboard "at a glance" cards. */
    @Data
    public static class DashboardSummary {
        private long productsTracked;
        private long activeAlerts;
        private long priceChangesLast24h;
    }
}
