package com.project.speedback.service;

import com.project.speedback.dto.BookingDTO;
import com.project.speedback.dto.BookingRequest;
import com.project.speedback.entity.Booking;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.repository.BookingRepository;
import com.project.speedback.repository.TeamMemberRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
    return bookingRepository.findAll().stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @Transactional
  public BookingDTO createBooking(BookingRequest request) {
    if (bookingRepository
        .findByBookerIdAndSlotNumber(request.getBookerId(), request.getSlotNumber())
        .isPresent()) {
      throw new IllegalArgumentException("You already have a booking for this slot.");
    }
    if (bookingRepository
        .findByBookieIdAndSlotNumber(request.getBookieId(), request.getSlotNumber())
        .isPresent()) {
      TeamMember bookie =
          teamMemberRepository
              .findById(request.getBookieId())
              .orElseThrow(() -> new IllegalArgumentException("Bookie not found."));
      throw new IllegalArgumentException(
          bookie.getName() + " is already booked as a bookie in this slot.");
    }

    Optional<Booking> bookieAsBookerCheck =
        bookingRepository.findByBookerIdAndSlotNumber(
            request.getBookieId(), request.getSlotNumber());
    if (bookieAsBookerCheck.isPresent()) {
      Booking conflictingBooking = bookieAsBookerCheck.get();

      String bookieName = conflictingBooking.getBooker().getName();
      String personTheyBooked = conflictingBooking.getBookie().getName();

      String errorMessage =
          String.format(
              "%s is not available in slot %d because they have already booked %s for that time.",
              bookieName, request.getSlotNumber(), personTheyBooked);
      throw new IllegalArgumentException(errorMessage);
    }

    TeamMember booker =
        teamMemberRepository
            .findById(request.getBookerId())
            .orElseThrow(() -> new IllegalArgumentException("Booker not found."));
    TeamMember bookie =
        teamMemberRepository
            .findById(request.getBookieId())
            .orElseThrow(() -> new IllegalArgumentException("Bookie not found."));

    Booking newBooking = new Booking(booker, bookie, request.getSlotNumber());
    Booking savedBooking = bookingRepository.save(newBooking);
    return convertToDto(savedBooking);
  }

  @Transactional
  public void deleteBooking(Long bookerId, int slotNumber) {
    Optional<Booking> bookingOptional =
        bookingRepository.findByBookerIdAndSlotNumber(bookerId, slotNumber);
    if (bookingOptional.isPresent()) {
      bookingRepository.delete(bookingOptional.get());
    } else {
      throw new IllegalArgumentException("No booking found for booker in this slot.");
    }
  }

  private BookingDTO convertToDto(Booking booking) {
    return modelMapper.map(booking, BookingDTO.class);
  }
}
