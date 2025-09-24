package com.project.speedBack.odt;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookingDTO {
    private Long id;
    private Long bookerId;
    private String bookerName;
    private Long bookieId;
    private String bookieName;
    private int slotNumber;

    public BookingDTO(Long id, Long bookerId, String bookerName, Long bookieId, String bookieName, int slotNumber) {
        this.id = id;
        this.bookerId = bookerId;
        this.bookerName = bookerName;
        this.bookieId = bookieId;
        this.bookieName = bookieName;
        this.slotNumber = slotNumber;
    }
}
