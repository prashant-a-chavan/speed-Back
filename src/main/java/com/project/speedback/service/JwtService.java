package com.project.speedback.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final String secret;
  private final long expirationMs;

  public JwtService(
      @Value("${security.jwt.secret}") String secret,
      @Value("${security.jwt.expiration-ms}") long expirationMs) {
    this.secret = secret;
    this.expirationMs = expirationMs;
  }

  public String generateToken(String username, Long memberId, String name) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("memberId", memberId);
    claims.put("name", name);
    return createToken(claims, username);
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public long getExpirationMs() {
    return expirationMs;
  }

  public boolean isTokenValid(String token, String username) {
    String tokenUsername = extractUsername(token);
    return tokenUsername.equals(username) && !isTokenExpired(token);
  }

  private String createToken(Map<String, Object> claims, String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
        .claims(claims)
        .subject(username)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }
}
