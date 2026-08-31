package com.project.speedback.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.speedback.dto.LoginRequest;
import com.project.speedback.dto.LoginResponse;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.exception.InvalidCredentialsException;
import com.project.speedback.repository.TeamMemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @Mock private TeamMemberRepository teamMemberRepository;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtService jwtService;

  private AuthService authService;

  @BeforeEach
  void setUp() {
    authService = new AuthService(teamMemberRepository, passwordEncoder, jwtService);
  }

  @Test
  void shouldReturnJwtTokenWhenCredentialsAreValid() {
    TeamMember member =
        TeamMember.builder()
            .id(1L)
            .name("Prashant")
            .username("Prashant")
            .passwordHash("hashed-pass")
            .build();

    LoginRequest request =
        LoginRequest.builder().username("Prashant").password("Prashant@123").build();

    when(teamMemberRepository.findByUsernameIgnoreCase("Prashant")).thenReturn(Optional.of(member));
    when(passwordEncoder.matches("Prashant@123", "hashed-pass")).thenReturn(true);
    when(jwtService.generateToken("Prashant", 1L, "Prashant")).thenReturn("jwt-token");
    when(jwtService.getExpirationMs()).thenReturn(3600000L);

    LoginResponse response = authService.login(request);

    assertEquals("jwt-token", response.getAccessToken());
    assertEquals("Bearer", response.getTokenType());
    assertEquals(3600L, response.getExpiresInSeconds());
    assertEquals(1L, response.getMemberId());
    verify(jwtService).generateToken("Prashant", 1L, "Prashant");
  }

  @Test
  void shouldThrowUnauthorizedWhenUsernameDoesNotExist() {
    LoginRequest request =
        LoginRequest.builder().username("Unknown").password("Unknown@123").build();
    when(teamMemberRepository.findByUsernameIgnoreCase("Unknown")).thenReturn(Optional.empty());

    InvalidCredentialsException exception =
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

    assertEquals("Invalid username or password", exception.getMessage());
  }

  @Test
  void shouldThrowUnauthorizedWhenPasswordIsInvalid() {
    TeamMember member =
        TeamMember.builder()
            .id(1L)
            .name("Prashant")
            .username("Prashant")
            .passwordHash("hashed-pass")
            .build();

    LoginRequest request = LoginRequest.builder().username("Prashant").password("wrong").build();

    when(teamMemberRepository.findByUsernameIgnoreCase("Prashant")).thenReturn(Optional.of(member));
    when(passwordEncoder.matches("wrong", "hashed-pass")).thenReturn(false);

    InvalidCredentialsException exception =
        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

    assertEquals("Invalid username or password", exception.getMessage());
  }
}
