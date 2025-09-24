package com.project.speedBack.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.lang.model.util.Elements;

@Entity
@Data
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"booker_id", "slotNumber"}),
        @UniqueConstraint(columnNames = {"bookie_id", "slotNumber"})
})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private TeamMember booker;

    @ManyToOne
    @JoinColumn(name = "bookie_id", nullable = false)
    private TeamMember bookie;

    private int slotNumber;

    public Booking(TeamMember booker, TeamMember bookie, int slotNumber) {
        this.booker = booker;
        this.bookie = bookie;
        this.slotNumber = slotNumber;
    }

}
