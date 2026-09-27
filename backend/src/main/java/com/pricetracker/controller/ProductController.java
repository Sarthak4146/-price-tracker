package com.pricetracker.controller;

import com.pricetracker.dto.ProductDtos.*;
import com.pricetracker.model.User;
import com.pricetracker.security.CurrentUserProvider;
import com.pricetracker.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * All endpoints here require a valid JWT (see SecurityConfig: anyRequest().authenticated()).
 * The frontend must send: Authorization: Bearer <token>
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CurrentUserProvider currentUserProvider;

    public ProductController(ProductService productService, CurrentUserProvider currentUserProvider) {
        this.productService = productService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody AddProductRequest request) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(productService.addProduct(user, request));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getMyProducts(
            @RequestParam(required = false) String search) {
        User user = currentUserProvider.getCurrentUser();
        if (search != null && !search.isBlank()) {
            return ResponseEntity.ok(productService.searchUserProducts(user.getId(), search));
        }
        return ResponseEntity.ok(productService.getUserProducts(user.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(productService.getProduct(id, user.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        User user = currentUserProvider.getCurrentUser();
        productService.deleteProduct(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/target-price")
    public ResponseEntity<ProductResponse> updateTargetPrice(
            @PathVariable Long id, @RequestBody UpdateTargetPriceRequest request) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(productService.updateTargetPrice(id, user.getId(), request.getTargetPrice()));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<PriceHistoryPoint>> getHistory(@PathVariable Long id) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(productService.getHistory(id, user.getId()));
    }

    @PostMapping("/{id}/refresh")
    public ResponseEntity<ProductResponse> refreshNow(@PathVariable Long id) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(productService.refreshPrice(id, user.getId()));
    }
}
