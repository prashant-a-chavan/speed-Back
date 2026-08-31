package com.project.speedback.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.speedback.dto.LoginRequest;
import com.project.speedback.dto.LoginResponse;
import com.project.speedback.exception.GlobalExceptionHandler;
import com.project.speedback.exception.InvalidCredentialsException;
import com.project.speedback.repository.TeamMemberRepository;
import com.project.speedback.service.AuthService;
import com.project.speedback.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private AuthService authService;

  @MockitoBean private JwtService jwtService;

  @MockitoBean private TeamMemberRepository teamMemberRepository;

  @Test
  void shouldLoginSuccessfully() throws Exception {
    LoginRequest request =
        LoginRequest.builder().username("Prashant").password("Prashant@123").build();

    LoginResponse response =
        LoginResponse.builder()
            .accessToken("token")
            .tokenType("Bearer")
            .expiresInSeconds(3600)
            .memberId(1L)
            .name("Prashant")
            .username("Prashant")
            .build();

    when(authService.login(any(LoginRequest.class))).thenReturn(response);

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken").value("token"))
        .andExpect(jsonPath("$.tokenType").value("Bearer"));
  }

  @Test
  void shouldReturnUnauthorizedForInvalidCredentials() throws Exception {
    LoginRequest request = LoginRequest.builder().username("Prashant").password("wrong").build();

    when(authService.login(any(LoginRequest.class)))
        .thenThrow(new InvalidCredentialsException("Invalid username or password"));

    mockMvc
        .perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Invalid username or password"));
  }
}
