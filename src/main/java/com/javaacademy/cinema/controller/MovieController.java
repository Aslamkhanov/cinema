package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.MovieService;
import com.javaacademy.cinema.service.interfaces.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movie")
@Tag(name = "Фильм", description = "Контроллер для работы с фильмами")
public class MovieController {
    private final MovieService movieService;
    private final SecurityService securityService;

    @Operation(summary = "Создание фильма",
            description = "Создание и сохранение фильма",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Фильм успешно сохранен",
                            content = @Content(schema = @Schema(implementation = MovieDto.class))),
                    @ApiResponse(responseCode = "403",
                            description = "Нет прав доступа, авторизуйтесь как администратор")
            })
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

    @Operation(summary = "Список всех фильмов", description = "Возвращает список всех фильмов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список фильмов",
                            content = @Content(array = @ArraySchema(
                                    schema = @Schema(implementation = MovieDto.class))))
            })
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovie() {
        return ResponseEntity.ok(movieService.selectAll());
    }
}
