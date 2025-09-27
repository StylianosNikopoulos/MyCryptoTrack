package com.mycryptotrack.auth.controller;

import com.mycryptotrack.auth.dto.UserDto;
import com.mycryptotrack.auth.model.User;
import com.mycryptotrack.auth.security.JwtUtil;
import com.mycryptotrack.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();

        userService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto dto) {
        return userService.findByUsername(dto.getUsername())
                .filter(u -> new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().matches(dto.getPassword(), u.getPassword()))
                .map(u -> ResponseEntity.ok(jwtUtil.generateToken(u.getUsername())))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
}

