package com.project.speedback.repository;

import com.project.speedback.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBySlotNumber(int slotNumber);

    Optional<Booking> findByBookerIdAndSlotNumber(Long bookerId, int slotNumber);

    Optional<Booking> findByBookieIdAndSlotNumber(Long bookieId, int slotNumber);

    List<Booking> findByBookerId(Long bookerId);

    void deleteByBookerIdAndSlotNumber(Long bookerId, int slotNumber);
}
