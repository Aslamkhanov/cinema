package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.CreateMovieDto;
import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.service.interfaces.ServicePlace;
import com.javaacademy.cinema.service.interfaces.ServiceSession;
import com.javaacademy.cinema.service.interfaces.ServiceMovie;
import com.javaacademy.cinema.service.interfaces.ServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final ServiceMovie serviceMovie;
    private final ServiceSession serviceSession;
    private final ServicePlace servicePlace;
    private final ServiceTicket serviceTicket;

    @PostMapping("/movie")
    public ResponseEntity<?> createMovie(@RequestBody CreateMovieDto createMovieDto,
                                         @RequestHeader(value = "user-token",
                                                 required = false) String userToken) {
        if (!"secretadmin123".equals(userToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        MovieDto movieDto = serviceMovie.saveMovie(createMovieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(movieDto);
    }

    @PostMapping("/session")
    public ResponseEntity<?> createSession(@RequestBody SessionDto sessionDto,
                                           @RequestHeader(value = "user-token",
                                                   required = false) String userToken) {
        if (!"secretadmin123".equals(userToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Session newSession = serviceSession.createSession(sessionDto);
        List<Place> places = servicePlace.findAllPlaces();
        serviceTicket.createTicketsForSession(newSession.getId(), places);

        return ResponseEntity.status(HttpStatus.CREATED).body(newSession);
    }

    @GetMapping("/ticket/saled")
    public ResponseEntity<?> getAllBoughtTickets(@RequestParam Integer sessionId,
                                                               @RequestHeader(value = "user-token",
                                                                    required = false) String userToken) {
        if (!"secretadmin123".equals(userToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(serviceTicket.findTicketsBoughtTrue(sessionId));
    }
}
