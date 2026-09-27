package com.pricetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point of the whole backend.
 *
 * @EnableScheduling turns on Spring's built-in scheduler so that
 * PriceCheckScheduler (see service package) can run automatically
 * every N minutes to re-check prices. This is what gives us
 * "real-time tracking" without needing any extra technology.
 */
@SpringBootApplication
@EnableScheduling
public class PriceTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(PriceTrackerApplication.class, args);
    }
}
