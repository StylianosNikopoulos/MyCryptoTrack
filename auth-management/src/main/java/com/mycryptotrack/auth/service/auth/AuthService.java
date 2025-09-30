package com.mycryptotrack.auth.service.auth;

import com.mycryptotrack.auth.dto.UserDto;
import com.mycryptotrack.auth.model.User;

public interface AuthService {
    User register(User user);
    String login(UserDto dto);
}
