package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.SecurityService;
import com.javaacademy.cinema.service.interfaces.SessionService;
import com.javaacademy.cinema.service.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final SecurityService securityService;

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionDto sessionDto,
                                           @RequestHeader(value = "user-token") String token) {
        try {
            securityService.checkIsAdmin(token);
            SessionDto newSessionDto = sessionService.save(sessionDto);
            ticketService.createTicketForSession(newSessionDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ForbiddenAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SessionDto>> getAllSession() {
        return ResponseEntity.ok(sessionService.selectAll());
    }

    @GetMapping("/{sessionId}/free-place")
    public ResponseEntity<List<String>> getFreePlacesForSession(@PathVariable Integer sessionId) {
        List<String> freePlaces = ticketService.selectFreePlace(sessionId);
        if (freePlaces == null || freePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(freePlaces);
    }
}
