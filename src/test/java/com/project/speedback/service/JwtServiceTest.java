package com.project.speedback.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

class JwtServiceTest {

  private static final String SECRET = "0123456789abcdef0123456789abcdef";

  @Test
  void shouldGenerateTokenAndExtractUsername() {
    JwtService jwtService = new JwtService(SECRET, 60_000L);

    String token = jwtService.generateToken("Prashant", 1L, "Prashant");

    assertEquals("Prashant", jwtService.extractUsername(token));
  }

  @Test
  void shouldValidateTokenForMatchingUsername() {
    JwtService jwtService = new JwtService(SECRET, 60_000L);
    String token = jwtService.generateToken("Prashant", 1L, "Prashant");

    assertTrue(jwtService.isTokenValid(token, "Prashant"));
    assertFalse(jwtService.isTokenValid(token, "SomeoneElse"));
  }

  @Test
  void shouldThrowForExpiredToken() throws InterruptedException {
    JwtService jwtService = new JwtService(SECRET, 1L);
    String token = jwtService.generateToken("Prashant", 1L, "Prashant");

    Thread.sleep(10L);

    assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, "Prashant"));
  }

  @Test
  void shouldThrowForMalformedToken() {
    JwtService jwtService = new JwtService(SECRET, 60_000L);

    assertThrows(JwtException.class, () -> jwtService.extractUsername("not-a-jwt"));
  }

  @Test
  void shouldReturnConfiguredExpirationMs() {
    JwtService jwtService = new JwtService(SECRET, 12_345L);

    assertEquals(12_345L, jwtService.getExpirationMs());
  }
}
