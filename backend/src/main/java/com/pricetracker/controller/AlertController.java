package com.pricetracker.controller;

import com.pricetracker.dto.AlertDtos.AlertResponse;
import com.pricetracker.model.User;
import com.pricetracker.security.CurrentUserProvider;
import com.pricetracker.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;
    private final CurrentUserProvider currentUserProvider;

    public AlertController(AlertService alertService, CurrentUserProvider currentUserProvider) {
        this.alertService = alertService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public ResponseEntity<List<AlertResponse>> getMyAlerts() {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(alertService.getUserAlerts(user.getId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<AlertResponse> markRead(@PathVariable Long id) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(alertService.markAsRead(id, user.getId()));
    }
}
