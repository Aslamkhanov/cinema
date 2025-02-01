package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.*;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.service.user.interfaces.UserServiceMovie;
import com.javaacademy.cinema.service.user.interfaces.UserServiceSession;
import com.javaacademy.cinema.service.user.interfaces.UserServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserServiceMovie serviceMovie;
    private final UserServiceSession serviceSession;
    private final UserServiceTicket serviceTicket;

    @GetMapping("/movie")
    public ResponseEntity<List<MovieDto>> getAllMovie() {
        return ResponseEntity.ok(serviceMovie.getAllMovie());
    }

    @GetMapping("/session")
    public ResponseEntity<List<SessionDto>> getAllSession() {
        return ResponseEntity.ok(serviceSession.getAllSession());
    }

    @GetMapping("/session/{sessionId}/free-place")
    public ResponseEntity<List<TicketDto>> getFreePlacesForSession(@PathVariable Integer sessionId) {
        List<TicketDto> sessionTickets = serviceTicket.findTicketsBoughtFalse(sessionId);
        if (sessionTickets == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(sessionTickets);
    }

    @PostMapping("/ticket/booking")
    public ResponseEntity<?> bookTicket(@RequestBody TicketRequest ticketRequest) {
        try {
            TicketResponse response = serviceTicket.bookTicket(ticketRequest.getSessionId(),
                    ticketRequest.getPlaceName());
            return ResponseEntity.ok(response);
        } catch (TicketAlreadyBookedException e) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        }
    }
}
