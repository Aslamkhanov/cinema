package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.MovieService;
import com.javaacademy.cinema.service.interfaces.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final SecurityService securityService;

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieDto movieDto,
                                         @RequestHeader(value = "user-token") String token) {
        try {
            securityService.checkIsAdmin(token);
            MovieDto savedMovie = movieService.save(movieDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
        } catch (ForbiddenAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovie() {
        return ResponseEntity.ok(movieService.selectAll());
    }
}
