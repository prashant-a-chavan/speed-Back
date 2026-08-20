package com.project.speedback.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
  @NotNull(message = "Booker ID is required")
  private Long bookerId;

  @NotNull(message = "Bookie ID is required")
  private Long bookieId;

  @Positive(message = "Slot number must be a positive integer")
  private int slotNumber;
}
