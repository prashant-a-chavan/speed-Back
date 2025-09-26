package com.project.speedback.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("SpeedBack API")
                .version("v1.0.0")
                .description(
                    "This is the official API documentation for the SpeedBack application. "
                        + "It provides endpoints for booking and managing feedback sessions."));
  }
}
