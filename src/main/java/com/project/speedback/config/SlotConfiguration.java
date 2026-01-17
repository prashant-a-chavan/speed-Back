package com.project.speedback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "speedback.slots")
@Data
public class SlotConfiguration {
  private int count = 3;
  private int durationMinutes = 15;
}
