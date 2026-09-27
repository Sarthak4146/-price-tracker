package com.pricetracker.security;

import com.pricetracker.model.User;
import com.pricetracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Convenience component: after JwtAuthFilter validates the token,
 * Spring Security's SecurityContext holds the username. Controllers
 * use this to turn that into the actual User entity, e.g. to scope
 * "give me MY products" queries.
 */
@Component
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found in database"));
    }
}
