package com.project.speedback.service;

import com.project.speedback.dto.LoginRequest;
import com.project.speedback.dto.LoginResponse;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.exception.InvalidCredentialsException;
import com.project.speedback.repository.TeamMemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid username or password";

  private final TeamMemberRepository teamMemberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthService(
      TeamMemberRepository teamMemberRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService) {
    this.teamMemberRepository = teamMemberRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  public LoginResponse login(LoginRequest request) {
    String username = request.getUsername().trim();
    if (username.isBlank()) {
      throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
    }

    TeamMember teamMember =
        teamMemberRepository
            .findByUsernameIgnoreCase(username)
            .orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE));

    if (!passwordEncoder.matches(request.getPassword(), teamMember.getPasswordHash())) {
      throw new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);
    }

    String token =
        jwtService.generateToken(
            teamMember.getUsername(), teamMember.getId(), teamMember.getName());

    return LoginResponse.builder()
        .accessToken(token)
        .tokenType("Bearer")
        .expiresInSeconds(jwtService.getExpirationMs() / 1000)
        .memberId(teamMember.getId())
        .name(teamMember.getName())
        .username(teamMember.getUsername())
        .build();
  }
}
