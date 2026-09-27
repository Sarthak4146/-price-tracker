package com.pricetracker.controller;

import com.pricetracker.dto.AlertDtos.DashboardSummary;
import com.pricetracker.model.User;
import com.pricetracker.repository.ProductRepository;
import com.pricetracker.security.CurrentUserProvider;
import com.pricetracker.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Backs the "at a glance" summary cards on the Dashboard screen. */
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ProductRepository productRepository;
    private final AlertService alertService;
    private final CurrentUserProvider currentUserProvider;

    public DashboardController(ProductRepository productRepository, AlertService alertService,
                                CurrentUserProvider currentUserProvider) {
        this.productRepository = productRepository;
        this.alertService = alertService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping("/summary")
    public ResponseEntity<DashboardSummary> getSummary() {
        User user = currentUserProvider.getCurrentUser();

        DashboardSummary summary = new DashboardSummary();
        summary.setProductsTracked(productRepository.countByUserId(user.getId()));
        summary.setActiveAlerts(alertService.getUnreadCount(user.getId()));
        summary.setPriceChangesLast24h(0); // simple placeholder metric, easy to extend later

        return ResponseEntity.ok(summary);
    }
}
