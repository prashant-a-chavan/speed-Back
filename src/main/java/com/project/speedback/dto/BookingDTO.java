package com.project.speedback.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDTO {
  private Long id;
  private Long bookerId;
  private String bookerName;
  private Long bookieId;
  private String bookieName;
  private int slotNumber;
}
