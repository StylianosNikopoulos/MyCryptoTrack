package com.mycryptotrack.auth.service;

import com.mycryptotrack.auth.dto.UserDto;
import com.mycryptotrack.auth.exception.CustomException;
import com.mycryptotrack.auth.entity.User;
import com.mycryptotrack.auth.repository.UserRepository;
import com.mycryptotrack.auth.security.JwtUtil;
import com.mycryptotrack.auth.service.auth.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void register_ShouldSaveUser_WhenEmailNotExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = authService.register(user);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        assertThat(captor.getValue().getPassword()).isEqualTo("encodedPassword");
        assertThat(captor.getValue().getRole()).isEqualTo("USER");
        assertThat(savedUser.getCreatedAt()).isNotNull();
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(CustomException.class, () -> authService.register(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsValid() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(dto.getEmail())).thenReturn("jwt-token");

        String token = authService.login(dto);

        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    void login_ShouldThrowException_WhenEmailNotFound() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> authService.login(dto));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIncorrect() {
        UserDto dto = new UserDto();
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword("encodedPassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(false);

        assertThrows(CustomException.class, () -> authService.login(dto));
    }
}
