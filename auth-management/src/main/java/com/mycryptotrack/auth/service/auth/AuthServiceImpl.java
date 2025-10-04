package com.mycryptotrack.auth.service.auth;

import com.mycryptotrack.auth.dto.UserDto;
import com.mycryptotrack.auth.exception.CustomException;
import com.mycryptotrack.auth.model.User;
import com.mycryptotrack.auth.repository.UserRepository;
import com.mycryptotrack.auth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new CustomException("Email already in use");
        }
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(UserDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid credentials");
        }

        return jwtUtil.generateToken(user.getEmail());
    }
}
