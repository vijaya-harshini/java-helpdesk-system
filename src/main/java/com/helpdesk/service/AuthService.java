
package com.helpdesk.service;

import com.helpdesk.model.User;
import com.helpdesk.repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        userRepository = new UserRepository();
    }

    // Register a new USER account
    public boolean register(
            String name,
            String email,
            String password) {

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is required.");
        }

        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException(
                    "Password must contain at least 8 characters."
            );
        }

        String cleanName = name.trim();
        String cleanEmail = email.trim().toLowerCase();

        // Check whether email already exists
        if (userRepository.findByEmail(cleanEmail) != null) {
            throw new IllegalArgumentException(
                    "An account with this email already exists."
            );
        }

        // Hash password using BCrypt
        String passwordHash = BCrypt.hashpw(
                password,
                BCrypt.gensalt(12)
        );

        // All public registrations receive USER role
        User user = new User(
                cleanName,
                cleanEmail,
                passwordHash,
                "USER"
        );

        return userRepository.save(user);
    }

    // Authenticate USER or ADMIN
    public User login(
            String email,
            String password,
            String selectedRole) {

        if (email == null || email.trim().isEmpty()
                || password == null || password.isEmpty()) {
            return null;
        }

        String cleanEmail = email.trim().toLowerCase();

        User user = userRepository.findByEmail(cleanEmail);

        if (user == null) {
            return null;
        }

        // Check the selected login portal
        if (!user.getRole().equalsIgnoreCase(selectedRole)) {
            return null;
        }

        // Verify password against stored BCrypt hash
        boolean passwordMatches = BCrypt.checkpw(
                password,
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            return null;
        }

        return user;
    }
}
