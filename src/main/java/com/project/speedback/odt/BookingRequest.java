package com.project.speedback.odt;

import lombok.Data;

@Data
public class BookingRequest {
    private Long bookerId;
    private Long bookieId;
    private int slotNumber;
}
