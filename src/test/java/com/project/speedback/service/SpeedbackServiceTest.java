package com.project.speedback.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.project.speedback.entity.Booking;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.odt.BookingDTO;
import com.project.speedback.odt.BookingRequest;
import com.project.speedback.repository.BookingRepository;
import com.project.speedback.repository.TeamMemberRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpeedbackServiceTest {

  @Mock private TeamMemberRepository teamMemberRepository;
  @Mock private BookingRepository bookingRepository;
  @InjectMocks private SpeedbackService speedbackService;

  TeamMember teamMember;

  @BeforeEach
  void setUp() {
    teamMember = TeamMember.builder().id(1L).name("Test").build();
  }

  @Test
  void shouldGetAllTeamMembers() {

    when(teamMemberRepository.findAll()).thenReturn(List.of(teamMember));

    List<TeamMember> allMembers = speedbackService.getAllTeamMembers(); // Step 1

    assertEquals(1, allMembers.size());
    assertTrue(allMembers.contains(teamMember));
    assertEquals("Test", allMembers.get(0).getName());
  }

  @Test
  void shouldGetAllBookings() {
    Booking booking =
        Booking.builder().id(1L).booker(teamMember).bookie(teamMember).slotNumber(2).build();

    when(bookingRepository.findAll()).thenReturn(List.of(booking));
    List<BookingDTO> allBookings = speedbackService.getAllBookings();

    assertEquals(1, allBookings.size());
    assertEquals("Test", allBookings.get(0).getBookieName());
    assertThat(allBookings.get(0).getBookerName(), is(equalTo("Test")));
    assertEquals(2, allBookings.get(0).getSlotNumber());
  }

  @Test
  void shouldCreateBooking() {
    BookingRequest bookingRequest =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(3).build();

    TeamMember bookie = TeamMember.builder().id(2L).name("Bookie").build();

    Booking booking =
        Booking.builder().id(1L).booker(teamMember).bookie(bookie).slotNumber(3).build();

    when(bookingRepository.findByBookerIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.empty());
    when(bookingRepository.findByBookieIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.empty());
    when(teamMemberRepository.findById(any())).thenReturn(Optional.of(teamMember));
    when(bookingRepository.save(any())).thenReturn(booking);

    BookingDTO createdBooking = speedbackService.createBooking(bookingRequest);

    assertEquals(1L, createdBooking.getBookerId());
    assertEquals(2L, createdBooking.getBookieId());
    assertEquals(3, createdBooking.getSlotNumber());
    assertEquals("Test", createdBooking.getBookerName());
    assertEquals("Bookie", createdBooking.getBookieName());
    verify(bookingRepository).save(any());
  }

  @Test
  void shouldThrowExceptionIfBookerAlreadyHasBooking() {
    BookingRequest bookingRequest =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(3).build();

    TeamMember bookie = TeamMember.builder().id(2L).name("Bookie").build();

    Booking booking =
        Booking.builder().id(1L).booker(teamMember).bookie(bookie).slotNumber(3).build();

    when(bookingRepository.findByBookerIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.of(booking));

    IllegalArgumentException exceptionReturned =
        assertThrows(
            IllegalArgumentException.class, () -> speedbackService.createBooking(bookingRequest));

    assertEquals("You already have a booking for this slot.", exceptionReturned.getMessage());
    verify(bookingRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionIfBookieAlreadyHasBooking() {
    BookingRequest bookingRequest =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(3).build();
    TeamMember bookie = TeamMember.builder().id(2L).name("Prashant").build();
    Booking booking =
        Booking.builder().id(1L).booker(teamMember).bookie(bookie).slotNumber(3).build();

    when(bookingRepository.findByBookieIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.of(booking));
    when(teamMemberRepository.findById(any())).thenReturn(Optional.of(bookie));

    IllegalArgumentException exceptionReturned =
        assertThrows(
            IllegalArgumentException.class, () -> speedbackService.createBooking(bookingRequest));
    assertEquals(
        "Prashant is already booked as a bookie in this slot.", exceptionReturned.getMessage());
    verify(bookingRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionIfBookieNotFound() {
    BookingRequest bookingRequest =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(3).build();
    TeamMember bookie = TeamMember.builder().id(2L).name("Prashant").build();
    Booking booking =
        Booking.builder().id(1L).booker(teamMember).bookie(bookie).slotNumber(3).build();

    when(bookingRepository.findByBookieIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.of(booking));
    when(teamMemberRepository.findById(any())).thenReturn(Optional.empty());

    IllegalArgumentException exceptionReturned =
        assertThrows(
            IllegalArgumentException.class, () -> speedbackService.createBooking(bookingRequest));
    assertEquals("Bookie not found.", exceptionReturned.getMessage());
    verify(bookingRepository, never()).save(any());
  }

  @Test
  void shouldThrowExceptionIfBookieIsAlreadyBookerInSlot() {
    BookingRequest bookingRequest =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(3).build();

    TeamMember bookie = TeamMember.builder().id(2L).name("Prashant").build();

    TeamMember someoneElse = TeamMember.builder().id(3L).name("John").build();

    Booking existingBooking =
        Booking.builder().id(1L).booker(bookie).bookie(someoneElse).slotNumber(3).build();

    when(bookingRepository.findByBookerIdAndSlotNumber(1L, 3)).thenReturn(Optional.empty());
    when(bookingRepository.findByBookieIdAndSlotNumber(2L, 3)).thenReturn(Optional.empty());
    when(bookingRepository.findByBookerIdAndSlotNumber(2L, 3))
        .thenReturn(Optional.of(existingBooking));

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> speedbackService.createBooking(bookingRequest));

    assertEquals(
        "Prashant is not available in slot 3 because they have already booked John for that time.",
        exception.getMessage());
    verify(bookingRepository, never()).save(any());
  }

  @Test
  void shouldDeleteBooking() {
    Booking booking =
        Booking.builder().id(1L).booker(teamMember).bookie(teamMember).slotNumber(2).build();

    when(bookingRepository.findByBookerIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.of(booking));
    doNothing().when(bookingRepository).delete(any());

    speedbackService.deleteBooking(1L, 2);

    verify(bookingRepository).delete(booking);
    verify(bookingRepository, never()).save(any());
  }

  @Test
  void shouldNotDeleteBookingIfNoBookingFound() {
    when(bookingRepository.findByBookerIdAndSlotNumber(anyLong(), anyInt()))
        .thenReturn(Optional.empty());

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> speedbackService.deleteBooking(1L, 2));

    assertEquals("No booking found for booker in this slot.", exception.getMessage());
    verify(bookingRepository, never()).delete(any());
    verify(bookingRepository, never()).save(any());
  }
}
