package com.project.speedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlotConfigDTO {
  private int count;
  private int durationMinutes;
}
