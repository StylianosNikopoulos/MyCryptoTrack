package com.mycryptotrack.auth.service.auth;

import com.mycryptotrack.auth.model.User;

import java.util.Optional;

public interface AuthService {
    User register(User user);
    Optional<User> findByEmail(String email);
}
