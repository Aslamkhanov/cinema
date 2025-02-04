package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.GetSessionDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.exception.AdminNotFoundException;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.mapper.MapperSession;
import com.javaacademy.cinema.service.interfaces.ConfigService;
import com.javaacademy.cinema.service.interfaces.ServicePlace;
import com.javaacademy.cinema.service.interfaces.ServiceSession;
import com.javaacademy.cinema.service.interfaces.ServiceTicket;
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
    private final ServiceSession serviceSession;
    private final ServiceTicket serviceTicket;
    private final ConfigService configService;

    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionDto sessionDto,
                                           @RequestHeader(value = "user-token") String token)
            throws EntityNotFoundException {
        try {
            configService.admin(token);
            serviceSession.createSessions(sessionDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AdminNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<GetSessionDto>> getAllSession() {
        return ResponseEntity.ok(serviceSession.getAllSession());
    }

    @GetMapping("/{sessionId}/free-place")
    public ResponseEntity<List<String>> getFreePlacesForSession(@PathVariable Integer sessionId) {
        List<String> freePlaces = serviceTicket.findFreePlaces(sessionId);
        if (freePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(freePlaces);

    }
}
