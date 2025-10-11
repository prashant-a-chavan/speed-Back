package com.project.speedback.odt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingRequest {
  private Long bookerId;
  private Long bookieId;
  private int slotNumber;
}
