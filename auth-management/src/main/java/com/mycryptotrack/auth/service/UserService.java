package com.mycryptotrack.auth.service;

import com.mycryptotrack.auth.exception.CustomException;
import com.mycryptotrack.auth.model.User;
import com.mycryptotrack.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new CustomException("Email already in use");
        }
        if (user.getRole() == null) user.setRole("USER");
        if (user.getCreatedAt() == null) user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
