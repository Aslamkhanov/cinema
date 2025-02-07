package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.ConfigService;
import com.javaacademy.cinema.service.interfaces.MovieService;
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
    private final ConfigService configService;

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieDto movieDto,
                                         @RequestHeader(value = "user-token") String token) {
        try {
            configService.checkIsAdmin(token);
            MovieDto savedMovie = movieService.save(movieDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMovie);
        } catch (ForbiddenAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovie() {
        return ResponseEntity.ok(movieService.selectAll());
    }
}
