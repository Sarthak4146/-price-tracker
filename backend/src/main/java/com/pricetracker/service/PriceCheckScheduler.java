package com.pricetracker.service;

import com.pricetracker.model.Product;
import com.pricetracker.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is what makes tracking "automatic" instead of only checking prices
 * when the user clicks refresh.
 *
 * @Scheduled(fixedRate = ...) tells Spring: "run this method every N
 * milliseconds, forever, in the background." No extra library needed -
 * this is built into Spring Boot (that's why PriceTrackerApplication has
 * @EnableScheduling on it).
 *
 * fixedRate is set generously (15 min) in application.properties because
 * hitting Amazon/Flipkart too often is what gets an IP blocked.
 */
@Component
public class PriceCheckScheduler {

    private static final Logger log = LoggerFactory.getLogger(PriceCheckScheduler.class);

    private final ProductRepository productRepository;
    private final ProductService productService;

    public PriceCheckScheduler(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Scheduled(fixedRateString = "${price-check.interval-ms:900000}")
    public void checkAllActiveProducts() {
        List<Product> products = productRepository.findByActiveTrue();
        log.info("Scheduled price check starting for {} active products", products.size());

        for (Product product : products) {
            try {
                productService.checkAndUpdatePrice(product);
            } catch (Exception e) {
                log.warn("Price check failed for product {}: {}", product.getId(), e.getMessage());
            }
        }
    }
}
