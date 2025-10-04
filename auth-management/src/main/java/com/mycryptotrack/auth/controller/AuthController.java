package com.mycryptotrack.auth.controller;

import com.mycryptotrack.auth.dto.UserDto;
import com.mycryptotrack.auth.model.User;
import com.mycryptotrack.auth.service.auth.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserDto dto) {
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
        authService.register(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(token);
    }
}
