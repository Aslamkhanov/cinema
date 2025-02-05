package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.GetSessionDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.exception.AdminNotFoundException;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.service.interfaces.ConfigService;
import com.javaacademy.cinema.service.interfaces.SessionService;
import com.javaacademy.cinema.service.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final ConfigService configService;

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionDto sessionDto,
                                           @RequestHeader(value = "user-token") String token) {
        try {
            configService.admin(token);
            SessionDto newSessionDto = sessionService.save(sessionDto);
            ticketService.createTicketForSession(newSessionDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AdminNotFoundException | EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GetSessionDto>> getAllSession() {
        return ResponseEntity.ok(sessionService.selectAll());
    }

    @GetMapping("/{sessionId}/free-place")
    public ResponseEntity<List<String>> getFreePlacesForSession(@PathVariable Integer sessionId) {
        List<String> freePlaces = ticketService.selectFreePlace(sessionId);
        if (freePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(freePlaces);

    }
}
