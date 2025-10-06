package com.mycryptotrack.alert.service;

import com.mycryptotrack.alert.dto.AlertDataDto;
import com.mycryptotrack.alert.enums.AlertType;
import com.mycryptotrack.alert.entity.AlertData;
import com.mycryptotrack.alert.repository.AlertRepository;
import com.mycryptotrack.alert.service.alert.AlertServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertServiceImplTest {

    @Mock
    private AlertRepository repository;

    private AlertServiceImpl service;

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        service = new AlertServiceImpl(repository);

        jwt = new Jwt(
                "token-value",
                Instant.now(),
                Instant.now().plusSeconds(3600),
                Map.of("alg", "HS256"),
                Map.of("sub", "test@email.com")
        );

        var auth = new UsernamePasswordAuthenticationToken(jwt, jwt.getTokenValue(), List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void createAlert_ShouldSaveSellAlertCorrectly() {
        // Arrange
        String symbol = "BTCUSDT";
        double targetPrice = 35000;
        String email = "test@email.com";
        AlertType type = AlertType.SELL;

        ArgumentCaptor<AlertData> captor = ArgumentCaptor.forClass(AlertData.class);
        when(repository.save(any(AlertData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.createAlert(symbol, targetPrice, email, type);

        // Assert
        verify(repository).save(captor.capture());
        AlertData saved = captor.getValue();

        assertThat(saved.getType()).isEqualTo(AlertType.SELL);
        assertThat(saved.getSymbol()).isEqualTo(symbol);
        assertThat(saved.getTargetPrice()).isEqualTo(targetPrice);
        assertThat(saved.getEmail()).isEqualTo("test@email.com");
        assertThat(saved.isTriggered()).isFalse();
        assertThat(result.getType()).isEqualTo(AlertType.SELL);
    }

    @Test
    void createAlert_ShouldSaveBuyAlertCorrectly() {
        // Arrange
        String symbol = "ETHUSDT";
        double targetPrice = 2500;
        String email = "test@email.com";
        AlertType type = AlertType.BUY;

        ArgumentCaptor<AlertData> captor = ArgumentCaptor.forClass(AlertData.class);
        when(repository.save(any(AlertData.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.createAlert(symbol, targetPrice, email, type);

        // Assert
        verify(repository).save(captor.capture());
        AlertData saved = captor.getValue();

        assertThat(saved.getType()).isEqualTo(AlertType.BUY);
        assertThat(saved.getSymbol()).isEqualTo(symbol);
        assertThat(saved.getTargetPrice()).isEqualTo(targetPrice);
        assertThat(saved.getEmail()).isEqualTo("test@email.com");
        assertThat(saved.isTriggered()).isFalse();
        assertThat(result.getType()).isEqualTo(AlertType.BUY);
    }

    @Test
    void getAllAlerts_ShouldReturnMappedDTOS(){
        // arrange
        AlertData alert1 = AlertData.builder()
                .id(1L).symbol("BTCUSDT").targetPrice(30000.0)
                .triggered(false).email("a@test.com").fetchedAt(Instant.now()).build();

        AlertData alert2 = AlertData.builder()
                .id(2L).symbol("ETHUSDT").targetPrice(2000.0)
                .triggered(true).email("b@test.com").fetchedAt(Instant.now()).build();

        when(repository.findAll()).thenReturn(List.of(alert1, alert2));

        // act
        List<AlertDataDto> results = service.getAllAlerts();

        // assert
        assertThat(results).hasSize(2);

        assertThat(results.get(0).getSymbol()).isEqualTo("BTCUSDT");
        assertThat(results.get(0).getTargetPrice()).isEqualTo(30000.0);
        assertThat(results.get(0).getEmail()).isEqualTo("a@test.com");
        assertThat(results.get(0).isTriggered()).isFalse();

        assertThat(results.get(1).getSymbol()).isEqualTo("ETHUSDT");
        assertThat(results.get(1).getTargetPrice()).isEqualTo(2000.0);
        assertThat(results.get(1).getEmail()).isEqualTo("b@test.com");
        assertThat(results.get(1).isTriggered()).isTrue();

        verify(repository, times(1)).findAll();
    }

    @Test
    void deleteAlert_ShouldCallRepositoryDelete(){
        Long id = 23L;
        service.deleteAlert(id);
        verify(repository,times(1)).deleteById(id);
    }
}





