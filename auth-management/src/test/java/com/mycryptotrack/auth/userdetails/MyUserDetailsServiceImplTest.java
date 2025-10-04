package com.mycryptotrack.auth.userdetails;

import com.mycryptotrack.auth.model.User;
import com.mycryptotrack.auth.repository.UserRepository;
import com.mycryptotrack.auth.service.userdetails.MyUserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MyUserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    private MyUserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new MyUserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole("USER");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername("test@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("test@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("encodedPassword");
        assertThat(userDetails.getAuthorities()).hasSize(1);
        assertThat(userDetails.getAuthorities().iterator().next().getAuthority()).isEqualTo("USER");
    }

    @Test
    void loadUserByUsername_ShouldThrow_WhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                service.loadUserByUsername("test@example.com")
        );
    }
}
