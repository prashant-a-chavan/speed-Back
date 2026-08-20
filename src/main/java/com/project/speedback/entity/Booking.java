package com.project.speedback.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"booker", "bookie"})
@Table(
    uniqueConstraints = {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Booking other)) return false;
    return id != null && id.equals(other.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getClass());
  }
}
