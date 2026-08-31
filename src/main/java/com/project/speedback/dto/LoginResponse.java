package com.project.speedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class LoginResponse {
  private String accessToken;
  private String tokenType;
  private long expiresInSeconds;
  private Long memberId;
  private String name;
  private String username;
}
