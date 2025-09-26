package com.project.speedback.controller;


import com.project.speedback.entity.TeamMember;
import com.project.speedback.odt.BookingDTO;
import com.project.speedback.odt.BookingRequest;
import com.project.speedback.service.SpeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "SpeedBack Operations", description = "Endpoints for managing team members and bookings")
public class SpeedbackController {

    private final SpeedbackService speedbackService;
    private final SimpMessagingTemplate messagingTemplate; // For sending WebSocket messages

    public SpeedbackController(SpeedbackService speedbackService, SimpMessagingTemplate messagingTemplate) {
        this.speedbackService = speedbackService;
        this.messagingTemplate = messagingTemplate;
    }

    @Operation(summary = "Get all team members", description = "Retrieves a list of all team members available for booking.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of team members",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TeamMember.class)))
    @GetMapping("/team-members")
    public List<TeamMember> getAllTeamMembers() {
        return speedbackService.getAllTeamMembers();
    }

    @Operation(summary = "Get all current bookings", description = "Retrieves a list of all existing bookings across all slots.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of bookings",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class)))
    @GetMapping("/bookings")
    public List<BookingDTO> getAllBookings() {
        return speedbackService.getAllBookings();
    }

    @Operation(summary = "Create a new booking", description = "Creates a new booking for a specific slot. Validates against existing bookings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking request (e.g., slot already taken, booking self)",
                    content = @Content)
    })
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        try {
            BookingDTO newBooking = speedbackService.createBooking(request);
            // Notify all subscribers about the new booking
            messagingTemplate.convertAndSend("/topic/bookings", speedbackService.getAllBookings());
            return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Delete a booking", description = "Deletes an existing booking for a specific booker and slot number.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request or booking not found", content = @Content)
    })
    @DeleteMapping("/bookings/{bookerId}/{slotNumber}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookerId, @PathVariable int slotNumber) {
        try {
            speedbackService.deleteBooking(bookerId, slotNumber);
            messagingTemplate.convertAndSend("/topic/bookings", speedbackService.getAllBookings());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
