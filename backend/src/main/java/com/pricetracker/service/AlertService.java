package com.pricetracker.service;

import com.pricetracker.dto.AlertDtos.AlertResponse;
import com.pricetracker.model.Alert;
import com.pricetracker.model.Product;
import com.pricetracker.repository.AlertRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    public void createPriceDropAlert(Product product, BigDecimal oldPrice, BigDecimal newPrice) {
        Alert alert = new Alert();
        alert.setProduct(product);
        alert.setUser(product.getUser());
        alert.setTriggeredPrice(newPrice);
        alert.setMessage(String.format("%s dropped to ₹%s (target was ₹%s)",
                product.getName(), newPrice, product.getTargetPrice()));
        alertRepository.save(alert);
    }

    public List<AlertResponse> getUserAlerts(Long userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    public long getUnreadCount(Long userId) {
        return alertRepository.countByUserIdAndReadFalse(userId);
    }

    public AlertResponse markAsRead(Long alertId, Long userId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        if (!alert.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Alert not found");
        }
        alert.setRead(true);
        alertRepository.save(alert);
        return toResponse(alert);
    }

    private AlertResponse toResponse(Alert a) {
        AlertResponse dto = new AlertResponse();
        dto.setId(a.getId());
        dto.setProductId(a.getProduct().getId());
        dto.setProductName(a.getProduct().getName());
        dto.setMessage(a.getMessage());
        dto.setTriggeredPrice(a.getTriggeredPrice());
        dto.setRead(a.isRead());
        dto.setCreatedAt(a.getCreatedAt());
        return dto;
    }
}
