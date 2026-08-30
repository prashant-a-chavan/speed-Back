package com.project.speedback.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.togglz.core.user.NoOpUserProvider;
import org.togglz.core.user.UserProvider;

@Configuration
public class TogglzConfig {

  @Bean
  public UserProvider userProvider() {
    return new NoOpUserProvider();
  }
}
