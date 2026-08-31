package com.project.speedback.config;

import com.project.speedback.entity.TeamMember;
import com.project.speedback.repository.TeamMemberRepository;
import com.project.speedback.service.JwtService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final TeamMemberRepository teamMemberRepository;

  public JwtAuthenticationFilter(JwtService jwtService, TeamMemberRepository teamMemberRepository) {
    this.jwtService = jwtService;
    this.teamMemberRepository = teamMemberRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = authHeader.substring(7);
    try {
      String username = jwtService.extractUsername(jwt);

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        TeamMember teamMember =
            teamMemberRepository.findByUsernameIgnoreCase(username).orElse(null);

        if (teamMember != null && jwtService.isTokenValid(jwt, teamMember.getUsername())) {
          UsernamePasswordAuthenticationToken authToken =
              new UsernamePasswordAuthenticationToken(teamMember.getUsername(), null, List.of());
          authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authToken);
        }
      }
    } catch (JwtException | IllegalArgumentException ex) {
      log.debug("Invalid JWT token: {}", ex.getMessage());
    }

    filterChain.doFilter(request, response);
  }
}
