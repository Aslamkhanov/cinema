package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.service.admin.interfaces.AdminServiceMovie;
import com.javaacademy.cinema.service.admin.interfaces.AdminServicePlace;
import com.javaacademy.cinema.service.admin.interfaces.AdminServiceSession;
import com.javaacademy.cinema.service.admin.interfaces.AdminServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Все операции ниже должны проходить, только если у запроса есть заголовок user-token
 * со значением secretadmin123. В случае если заголовка нет или значение не то,
 * должна возвращаться 403 ошибка (Forbidden).
 * 1. Создать endpoint POST /movie на создание фильма.
 * Данный endpoint должен создавать в бд фильм. Возвращает созданный фильм с id.
 * 2. Создать enpoint POST /session на создание сеанса. Данный endpoint:
 * 2.1 Создаст сеанс
 * 2.2 Получит все места
 * 2.3 Создаст некупленные билеты на каждое место в зале для этого сеанса
 * 3. Создать endpoint GET /ticket/saled который будет показывать купленные билеты.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final AdminServiceMovie ServiceMovie;
    private final AdminServiceSession serviceSession;
    private final AdminServicePlace servicePlace;
    private final AdminServiceTicket serviceTicket;

    @PostMapping("/movie")
    public ResponseEntity<?> createMovie(@RequestBody MovieDto movieDto,
                                         @RequestHeader(value = "user-token",
                                                 required = false) String userToken) {
        if (!"secretadmin123".equals(userToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Movie newMovie = ServiceMovie.saveMovie(movieDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMovie);
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
    public ResponseEntity<List<Ticket>> getAllBoughtTickets(@RequestParam Integer sessionId,
                                                            @RequestHeader(value = "user-token",
                                                                    required = false) String userToken) {
        if (!"secretadmin123".equals(userToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(serviceTicket.findTicketsBoughtTrue(sessionId));
    }
}
