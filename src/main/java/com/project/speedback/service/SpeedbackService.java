package com.project.speedback.service;

import com.project.speedback.dto.BookingDTO;
import com.project.speedback.dto.BookingRequest;
import com.project.speedback.entity.Booking;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.exception.BookingConflictException;
import com.project.speedback.exception.BookingNotFoundException;
import com.project.speedback.repository.BookingRepository;
import com.project.speedback.repository.TeamMemberRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SpeedbackService {

  private final TeamMemberRepository teamMemberRepository;
  private final BookingRepository bookingRepository;
  private final ModelMapper modelMapper;

  public SpeedbackService(
      TeamMemberRepository teamMemberRepository,
      BookingRepository bookingRepository,
      ModelMapper modelMapper) {
    this.teamMemberRepository = teamMemberRepository;
    this.bookingRepository = bookingRepository;
    this.modelMapper = modelMapper;
  }

  public List<TeamMember> getAllTeamMembers() {
    return teamMemberRepository.findAll();
  }

  public List<BookingDTO> getAllBookings() {
    return bookingRepository.findAll().stream().map(this::convertToDto).toList();
  }

  @Transactional
  public BookingDTO createBooking(BookingRequest request) {
    if (request.getBookerId().equals(request.getBookieId())) {
      throw new BookingConflictException("You cannot book a session with yourself.");
    }

    if (bookingRepository
        .findByBookerIdAndSlotNumber(request.getBookerId(), request.getSlotNumber())
        .isPresent()) {
      throw new BookingConflictException("You already have a booking for this slot.");
    }

    if (bookingRepository
        .findByBookieIdAndSlotNumber(request.getBookieId(), request.getSlotNumber())
        .isPresent()) {
      TeamMember bookie =
          teamMemberRepository
              .findById(request.getBookieId())
              .orElseThrow(() -> new BookingNotFoundException("Bookie not found."));
      throw new BookingConflictException(
          bookie.getName() + " is already booked as a bookie in this slot.");
    }

    bookingRepository
        .findByBookerIdAndSlotNumber(request.getBookieId(), request.getSlotNumber())
        .ifPresent(
            conflictingBooking -> {
              String bookieName = conflictingBooking.getBooker().getName();
              String personTheyBooked = conflictingBooking.getBookie().getName();
              throw new BookingConflictException(
                  String.format(
                      "%s is not available in slot %d because they have already booked %s for that"
                          + " time.",
                      bookieName, request.getSlotNumber(), personTheyBooked));
            });

    TeamMember booker =
        teamMemberRepository
            .findById(request.getBookerId())
            .orElseThrow(() -> new BookingNotFoundException("Booker not found."));
    TeamMember bookie =
        teamMemberRepository
            .findById(request.getBookieId())
            .orElseThrow(() -> new BookingNotFoundException("Bookie not found."));

    Booking newBooking =
        Booking.builder().booker(booker).bookie(bookie).slotNumber(request.getSlotNumber()).build();
    Booking savedBooking = bookingRepository.save(newBooking);
    log.info(
        "Booking created: booker={}, bookie={}, slot={}",
        booker.getName(),
        bookie.getName(),
        request.getSlotNumber());
    return convertToDto(savedBooking);
  }

  @Transactional
  public void deleteBooking(Long bookerId, int slotNumber) {
    Booking booking =
        bookingRepository
            .findByBookerIdAndSlotNumber(bookerId, slotNumber)
            .orElseThrow(
                () -> new BookingNotFoundException("No booking found for booker in this slot."));
    bookingRepository.delete(booking);
    log.info("Booking deleted: bookerId={}, slot={}", bookerId, slotNumber);
  }

  private BookingDTO convertToDto(Booking booking) {
    return modelMapper.map(booking, BookingDTO.class);
  }
}
