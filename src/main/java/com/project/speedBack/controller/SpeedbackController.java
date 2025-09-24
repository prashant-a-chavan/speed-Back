package com.project.speedBack.controller;


import com.project.speedBack.entity.TeamMember;
import com.project.speedBack.odt.BookingDTO;
import com.project.speedBack.odt.BookingRequest;
import com.project.speedBack.service.SpeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000") // Allow your React app to connect
public class SpeedbackController {

    private final SpeedbackService speedbackService;
    private final SimpMessagingTemplate messagingTemplate; // For sending WebSocket messages

    public SpeedbackController(SpeedbackService speedbackService, SimpMessagingTemplate messagingTemplate) {
        this.speedbackService = speedbackService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/team-members")
    public List<TeamMember> getAllTeamMembers() {
        return speedbackService.getAllTeamMembers();
    }

    @GetMapping("/bookings")
    public List<BookingDTO> getAllBookings() {
        return speedbackService.getAllBookings();
    }

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

    @DeleteMapping("/bookings/{giverId}/{slotNumber}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long giverId, @PathVariable int slotNumber) {
        try {
            speedbackService.deleteBooking(giverId, slotNumber);
            // Notify all subscribers about the updated bookings
            messagingTemplate.convertAndSend("/topic/bookings", speedbackService.getAllBookings());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
