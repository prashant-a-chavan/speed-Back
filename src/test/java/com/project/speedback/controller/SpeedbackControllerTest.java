package com.project.speedback.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.speedback.config.SlotConfiguration;
import com.project.speedback.dto.BookingDTO;
import com.project.speedback.dto.BookingRequest;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.exception.BookingConflictException;
import com.project.speedback.exception.BookingNotFoundException;
import com.project.speedback.exception.GlobalExceptionHandler;
import com.project.speedback.service.SpeedbackService;
import com.project.speedback.toggles.ServiceFeatures;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

@WebMvcTest(SpeedbackController.class)
@Import(GlobalExceptionHandler.class)
class SpeedbackControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private SpeedbackService speedbackService;

  @MockitoBean private SimpMessagingTemplate messagingTemplate;

  @MockitoBean private SlotConfiguration slotConfiguration;

  @BeforeEach
  void setUp() {
    when(slotConfiguration.getCount()).thenReturn(6);
    when(slotConfiguration.getDurationMinutes()).thenReturn(20);
  }

  @Test
  void shouldGetAllTeamMembers() throws Exception {
    TeamMember alice = TeamMember.builder().id(1L).name("Alice").build();
    when(speedbackService.getAllTeamMembers()).thenReturn(List.of(alice));

    mockMvc
        .perform(get("/api/team-members"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Alice"));

    verify(speedbackService).getAllTeamMembers();
  }

  @Test
  void shouldGetSlotConfiguration() throws Exception {
    mockMvc
        .perform(get("/api/config/slots"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.count").value(6))
        .andExpect(jsonPath("$.durationMinutes").value(20));
  }

  @Test
  void shouldGetAllBookings() throws Exception {
    BookingDTO booking = new BookingDTO(1L, 1L, "Booker", 2L, "Bookie", 3);
    when(speedbackService.getAllBookings()).thenReturn(List.of(booking));

    mockMvc
        .perform(get("/api/bookings"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].bookerName").value("Booker"));

    verify(speedbackService).getAllBookings();
  }

  @Test
  void shouldCreateBookingAndBroadcastWhenRequestIsValid() throws Exception {
    BookingRequest request =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(2).build();
    BookingDTO created = new BookingDTO(7L, 1L, "Booker", 2L, "Bookie", 2);
    List<BookingDTO> allBookings = List.of(created);

    when(speedbackService.createBooking(any(BookingRequest.class))).thenReturn(created);
    when(speedbackService.getAllBookings()).thenReturn(allBookings);

    mockMvc
        .perform(
            post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(7L))
        .andExpect(jsonPath("$.bookerName").value("Booker"));

    verify(speedbackService).createBooking(any(BookingRequest.class));
    verify(speedbackService).getAllBookings();
    verify(messagingTemplate).convertAndSend(eq("/topic/bookings"), eq(allBookings));
  }

  @Test
  void shouldReturnBadRequestWhenCreateBookingFailsValidation() throws Exception {
    BookingRequest request =
        BookingRequest.builder().bookerId(1L).bookieId(1L).slotNumber(2).build();
    when(speedbackService.createBooking(any(BookingRequest.class)))
        .thenThrow(new BookingConflictException("Invalid booking request"));

    mockMvc
        .perform(
            post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("Invalid booking request"));

    verify(speedbackService).createBooking(any(BookingRequest.class));
    verify(speedbackService, never()).getAllBookings();
    verify(messagingTemplate, never()).convertAndSend(eq("/topic/bookings"), any(Object.class));
  }

  @Test
  void shouldReturnBadRequestForBeanValidationErrors() throws Exception {
    // Missing required fields - slotNumber = 0 (non-positive) should trigger validation
    BookingRequest invalidRequest = BookingRequest.builder().slotNumber(0).build();

    mockMvc
        .perform(
            post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest());

    verify(speedbackService, never()).createBooking(any());
  }

  @Test
  void shouldReturnNotFoundWhenDeleteFeatureIsDisabled() throws Exception {
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS)).thenReturn(false);

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      mockMvc.perform(delete("/api/bookings/1/2")).andExpect(status().isNotFound());

      verify(speedbackService, never()).deleteBooking(any(), any(Integer.class));
      verify(speedbackService, never()).getAllBookings();
    }
  }

  @Test
  void shouldDeleteBookingAndBroadcastWhenFeatureIsEnabled() throws Exception {
    BookingDTO booking = new BookingDTO(1L, 1L, "Booker", 2L, "Bookie", 1);
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS)).thenReturn(true);
    when(speedbackService.getAllBookings()).thenReturn(List.of(booking));

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      mockMvc.perform(delete("/api/bookings/1/1")).andExpect(status().isNoContent());

      verify(speedbackService).deleteBooking(1L, 1);
      verify(speedbackService).getAllBookings();
      verify(messagingTemplate).convertAndSend(eq("/topic/bookings"), eq(List.of(booking)));
    }
  }

  @Test
  void shouldReturnNotFoundWhenDeleteBookingThrowsNotFound() throws Exception {
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS)).thenReturn(true);
    doThrow(new BookingNotFoundException("No booking found for booker in this slot."))
        .when(speedbackService)
        .deleteBooking(1L, 9);

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      mockMvc
          .perform(delete("/api/bookings/1/9"))
          .andExpect(status().isNotFound())
          .andExpect(content().string("No booking found for booker in this slot."));

      verify(speedbackService).deleteBooking(1L, 9);
      verify(speedbackService, never()).getAllBookings();
    }
  }
}
