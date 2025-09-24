package com.project.speedBack.service;

import com.project.speedBack.entity.Booking;
import com.project.speedBack.entity.TeamMember;
import com.project.speedBack.odt.BookingDTO;
import com.project.speedBack.odt.BookingRequest;
import com.project.speedBack.repository.BookingRepository;
import com.project.speedBack.repository.TeamMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SpeedbackService {

    private final TeamMemberRepository teamMemberRepository;
    private final BookingRepository bookingRepository;

    public SpeedbackService(TeamMemberRepository teamMemberRepository, BookingRepository bookingRepository) {
        this.teamMemberRepository = teamMemberRepository;
        this.bookingRepository = bookingRepository;
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
        if (bookingRepository.findByBookerIdAndSlotNumber(request.getBookerId(), request.getSlotNumber()).isPresent()) {
            throw new IllegalArgumentException("You already have a booking for this slot.");
        }
        if (bookingRepository.findByBookieIdAndSlotNumber(request.getBookieId(), request.getSlotNumber()).isPresent()) {
            TeamMember bookie = teamMemberRepository.findById(request.getBookieId())
                    .orElseThrow(() -> new IllegalArgumentException("Bookie not found."));
            throw new IllegalArgumentException(bookie.getName() + " is already booked as a bookie in this slot.");
        }

        Optional<Booking> bookieAsBookerCheck = bookingRepository.findByBookerIdAndSlotNumber(request.getBookieId(), request.getSlotNumber());
        if (bookieAsBookerCheck.isPresent()) {
            Booking conflictingBooking = bookieAsBookerCheck.get();

            String bookieName = conflictingBooking.getBooker().getName();
            String personTheyBooked = conflictingBooking.getBookie().getName();

            String errorMessage = String.format(
                    "%s is not available in slot %d because they have already booked %s for that time.",
                    bookieName,
                    request.getSlotNumber(),
                    personTheyBooked
            );
            throw new IllegalArgumentException(errorMessage);
        }

        TeamMember booker = teamMemberRepository.findById(request.getBookerId())
                .orElseThrow(() -> new IllegalArgumentException("Booker not found."));
        TeamMember bookie = teamMemberRepository.findById(request.getBookieId())
                .orElseThrow(() -> new IllegalArgumentException("Bookie not found."));

        Booking newBooking = new Booking(booker, bookie, request.getSlotNumber());
        Booking savedBooking = bookingRepository.save(newBooking);
        return convertToDto(savedBooking);
    }

    @Transactional
    public void deleteBooking(Long bookerId, int slotNumber) {
        Optional<Booking> bookingOptional = bookingRepository.findByBookerIdAndSlotNumber(bookerId, slotNumber);
        if (bookingOptional.isPresent()) {
            bookingRepository.delete(bookingOptional.get());
        } else {
            throw new IllegalArgumentException("No booking found for booker in this slot.");
        }
    }

    private BookingDTO convertToDto(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getBooker().getId(),
                booking.getBooker().getName(),
                booking.getBookie().getId(),
                booking.getBookie().getName(),
                booking.getSlotNumber()
        );
    }
}