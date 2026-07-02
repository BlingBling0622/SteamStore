package com.steamlibrary.service;

import com.steamlibrary.dto.RegisterRequest;
import com.steamlibrary.model.User;
import com.steamlibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    // ---- Online / offline tracking ----

    /** A user is "online" if they had activity within this many minutes. */
    public static final long ONLINE_WINDOW_MINUTES = 2L;

    public static boolean isOnline(LocalDateTime lastSeenAt) {
        return lastSeenAt != null
                && lastSeenAt.isAfter(LocalDateTime.now().minusMinutes(ONLINE_WINDOW_MINUTES));
    }

    public static boolean isOnline(User user) {
        return user != null && isOnline(user.getLastSeenAt());
    }

    @Transactional
    public void updateLastSeenAt(Long userId) {
        userRepository.updateLastSeenAt(userId, LocalDateTime.now());
    }

    @Transactional
    public void clearLastSeenAt(Long userId) {
        userRepository.clearLastSeenAt(userId);
    }
}
