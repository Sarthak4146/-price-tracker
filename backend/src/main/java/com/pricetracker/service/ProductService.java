package com.pricetracker.service;

import com.pricetracker.dto.ProductDtos.*;
import com.pricetracker.model.PriceHistory;
import com.pricetracker.model.Product;
import com.pricetracker.model.User;
import com.pricetracker.repository.PriceHistoryRepository;
import com.pricetracker.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final PriceHistoryRepository priceHistoryRepository;
    private final PriceFetcherService priceFetcherService;
    private final AlertService alertService;

    public ProductService(ProductRepository productRepository,
                           PriceHistoryRepository priceHistoryRepository,
                           PriceFetcherService priceFetcherService,
                           AlertService alertService) {
        this.productRepository = productRepository;
        this.priceHistoryRepository = priceHistoryRepository;
        this.priceFetcherService = priceFetcherService;
        this.alertService = alertService;
    }

    /** Add Product screen: user pastes a URL (+ optional target price). */
    public ProductResponse addProduct(User user, AddProductRequest request) {
        String platform = priceFetcherService.detectPlatform(request.getUrl());
        if (platform.equals("UNKNOWN")) {
            throw new IllegalArgumentException("Only Amazon and Flipkart links are supported right now");
        }

        PriceFetcherService.FetchedProduct fetched = priceFetcherService.fetchProduct(request.getUrl());

        Product product = new Product();
        product.setUser(user);
        product.setUrl(request.getUrl());
        product.setName(fetched.name());
        product.setImageUrl(fetched.imageUrl());
        product.setPlatform(platform);
        product.setCurrentPrice(fetched.price());
        product.setTargetPrice(request.getTargetPrice());
        product.setActive(true);
        product.setLastCheckedAt(LocalDateTime.now());

        product = productRepository.save(product);

        // Record the very first price point immediately
        savePricePoint(product, fetched.price());

        return toResponse(product);
    }

    public List<ProductResponse> getUserProducts(Long userId) {
        return productRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).toList();
    }

    public List<ProductResponse> searchUserProducts(Long userId, String keyword) {
        return productRepository.findByUserIdAndNameContainingIgnoreCase(userId, keyword)
                .stream().map(this::toResponse).toList();
    }

    public ProductResponse getProduct(Long id, Long userId) {
        Product product = productRepository.findByIdAndUserId(id, userId);
        if (product == null) throw new IllegalArgumentException("Product not found");
        return toResponse(product);
    }

    public void deleteProduct(Long id, Long userId) {
        Product product = productRepository.findByIdAndUserId(id, userId);
        if (product == null) throw new IllegalArgumentException("Product not found");
        productRepository.delete(product);
    }

    public ProductResponse updateTargetPrice(Long id, Long userId, BigDecimal targetPrice) {
        Product product = productRepository.findByIdAndUserId(id, userId);
        if (product == null) throw new IllegalArgumentException("Product not found");
        product.setTargetPrice(targetPrice);
        productRepository.save(product);
        return toResponse(product);
    }

    public List<PriceHistoryPoint> getHistory(Long productId, Long userId) {
        Product product = productRepository.findByIdAndUserId(productId, userId);
        if (product == null) throw new IllegalArgumentException("Product not found");

        return priceHistoryRepository.findByProductIdOrderByCheckedAtAsc(productId)
                .stream()
                .map(ph -> new PriceHistoryPoint(ph.getPrice(), ph.getCheckedAt()))
                .toList();
    }

    /** Manual "refresh now" button on the frontend, in addition to the scheduler. */
    public ProductResponse refreshPrice(Long productId, Long userId) {
        Product product = productRepository.findByIdAndUserId(productId, userId);
        if (product == null) throw new IllegalArgumentException("Product not found");

        checkAndUpdatePrice(product);
        return toResponse(product);
    }

    /** Core logic shared by manual refresh AND the scheduled job. */
    public void checkAndUpdatePrice(Product product) {
        priceFetcherService.fetchCurrentPrice(product.getUrl()).ifPresent(newPrice -> {
            BigDecimal oldPrice = product.getCurrentPrice();
            product.setCurrentPrice(newPrice);
            product.setLastCheckedAt(LocalDateTime.now());
            productRepository.save(product);

            savePricePoint(product, newPrice);

            // Trigger an alert if we crossed the target price
            if (product.getTargetPrice() != null && newPrice.compareTo(product.getTargetPrice()) <= 0) {
                alertService.createPriceDropAlert(product, oldPrice, newPrice);
            }
        });
    }

    private void savePricePoint(Product product, BigDecimal price) {
        if (price == null) return;
        PriceHistory ph = new PriceHistory();
        ph.setProduct(product);
        ph.setPrice(price);
        priceHistoryRepository.save(ph);
    }

    private ProductResponse toResponse(Product p) {
        ProductResponse dto = new ProductResponse();
        dto.setId(p.getId());
        dto.setUrl(p.getUrl());
        dto.setName(p.getName());
        dto.setImageUrl(p.getImageUrl());
        dto.setPlatform(p.getPlatform());
        dto.setCurrentPrice(p.getCurrentPrice());
        dto.setTargetPrice(p.getTargetPrice());
        dto.setActive(p.isActive());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setLastCheckedAt(p.getLastCheckedAt());

        dto.setMinPrice(priceHistoryRepository.findMinPrice(p.getId()));
        dto.setMaxPrice(priceHistoryRepository.findMaxPrice(p.getId()));
        dto.setAvgPrice(priceHistoryRepository.findAvgPrice(p.getId()));

        return dto;
    }
}
