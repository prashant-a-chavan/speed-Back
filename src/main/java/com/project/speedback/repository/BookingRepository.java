package com.project.speedback.repository;

import com.project.speedback.entity.Booking;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  Optional<Booking> findByBookerIdAndSlotNumber(Long bookerId, int slotNumber);

  Optional<Booking> findByBookieIdAndSlotNumber(Long bookieId, int slotNumber);
}
