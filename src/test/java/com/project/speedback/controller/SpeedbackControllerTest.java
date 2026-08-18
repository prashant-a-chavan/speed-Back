package com.project.speedback.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.speedback.config.SlotConfiguration;
import com.project.speedback.dto.BookingDTO;
import com.project.speedback.dto.BookingRequest;
import com.project.speedback.dto.SlotConfigDTO;
import com.project.speedback.entity.TeamMember;
import com.project.speedback.service.SpeedbackService;
import com.project.speedback.toggles.ServiceFeatures;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.togglz.core.context.FeatureContext;
import org.togglz.core.manager.FeatureManager;

@ExtendWith(MockitoExtension.class)
class SpeedbackControllerTest {

  @Mock private SpeedbackService speedbackService;

  @Mock private SimpMessagingTemplate messagingTemplate;

  private SpeedbackController controller;

  @BeforeEach
  void setUp() {
    SlotConfiguration slotConfiguration = new SlotConfiguration();
    slotConfiguration.setCount(6);
    slotConfiguration.setDurationMinutes(20);
    controller = new SpeedbackController(speedbackService, messagingTemplate, slotConfiguration);
  }

  @Test
  void shouldGetAllTeamMembers() {
    TeamMember alice = TeamMember.builder().id(1L).name("Alice").build();
    when(speedbackService.getAllTeamMembers()).thenReturn(List.of(alice));

    List<TeamMember> result = controller.getAllTeamMembers();

    assertEquals(1, result.size());
    assertEquals("Alice", result.get(0).getName());
    verify(speedbackService).getAllTeamMembers();
  }

  @Test
  void shouldGetSlotConfiguration() {
    SlotConfigDTO result = controller.getSlotConfiguration();

    assertEquals(6, result.getCount());
    assertEquals(20, result.getDurationMinutes());
  }

  @Test
  void shouldGetAllBookings() {
    BookingDTO booking = new BookingDTO(1L, 1L, "Booker", 2L, "Bookie", 3);
    when(speedbackService.getAllBookings()).thenReturn(List.of(booking));

    List<BookingDTO> result = controller.getAllBookings();

    assertEquals(1, result.size());
    assertEquals("Booker", result.get(0).getBookerName());
    verify(speedbackService).getAllBookings();
  }

  @Test
  void shouldCreateBookingAndBroadcastWhenRequestIsValid() {
    BookingRequest request =
        BookingRequest.builder().bookerId(1L).bookieId(2L).slotNumber(2).build();
    BookingDTO created = new BookingDTO(7L, 1L, "Booker", 2L, "Bookie", 2);
    List<BookingDTO> allBookings = List.of(created);

    when(speedbackService.createBooking(request)).thenReturn(created);
    when(speedbackService.getAllBookings()).thenReturn(allBookings);

    ResponseEntity<?> response = controller.createBooking(request);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertInstanceOf(BookingDTO.class, response.getBody());
    assertEquals(created, response.getBody());
    verify(speedbackService).createBooking(request);
    verify(speedbackService).getAllBookings();
    verify(messagingTemplate).convertAndSend("/topic/bookings", allBookings);
  }

  @Test
  void shouldReturnBadRequestWhenCreateBookingFailsValidation() {
    BookingRequest request =
        BookingRequest.builder().bookerId(1L).bookieId(1L).slotNumber(2).build();
    when(speedbackService.createBooking(request))
        .thenThrow(new IllegalArgumentException("Invalid booking request"));

    ResponseEntity<?> response = controller.createBooking(request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Invalid booking request", response.getBody());
    verify(speedbackService).createBooking(request);
    verify(speedbackService, never()).getAllBookings();
    verify(messagingTemplate, never()).convertAndSend(eq("/topic/bookings"), any(Object.class));
  }

  @Test
  void shouldReturnNotFoundWhenDeleteFeatureIsDisabled() {
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS)).thenReturn(false);

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      ResponseEntity<?> response = controller.deleteBooking(1L, 2);

      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      assertEquals("Delete feature is currently disabled", response.getBody());
      verify(speedbackService, never()).deleteBooking(any(), any(Integer.class));
      verify(speedbackService, never()).getAllBookings();
      verify(messagingTemplate, never()).convertAndSend(eq("/topic/bookings"), any(Object.class));
    }
  }

  @Test
  void shouldDeleteBookingAndBroadcastWhenFeatureIsEnabled() {
    BookingDTO booking = new BookingDTO(1L, 1L, "Booker", 2L, "Bookie", 1);
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS)).thenReturn(true);
    when(speedbackService.getAllBookings()).thenReturn(List.of(booking));

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      ResponseEntity<?> response = controller.deleteBooking(1L, 1);

      assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
      assertTrue(response.getBody() == null);
      verify(speedbackService).deleteBooking(1L, 1);
      verify(speedbackService).getAllBookings();
      verify(messagingTemplate).convertAndSend("/topic/bookings", List.of(booking));
    }
  }

  @Test
  void shouldReturnNotFoundWhenDeleteBookingThrowsValidationError() {
    FeatureManager featureManager = org.mockito.Mockito.mock(FeatureManager.class);
    when(featureManager.isActive(ServiceFeatures.REMOVE_BOOKINGS)).thenReturn(true);
    doThrow(new IllegalArgumentException("No booking found for booker in this slot."))
        .when(speedbackService)
        .deleteBooking(1L, 9);

    try (MockedStatic<FeatureContext> mockedFeatureContext =
        org.mockito.Mockito.mockStatic(FeatureContext.class)) {
      mockedFeatureContext.when(FeatureContext::getFeatureManager).thenReturn(featureManager);

      ResponseEntity<?> response = controller.deleteBooking(1L, 9);

      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      assertEquals("No booking found for booker in this slot.", response.getBody());
      verify(speedbackService).deleteBooking(1L, 9);
      verify(speedbackService, never()).getAllBookings();
      verify(messagingTemplate, never()).convertAndSend(eq("/topic/bookings"), any(Object.class));
    }
  }
}
