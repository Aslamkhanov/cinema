package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.SecurityService;
import com.javaacademy.cinema.service.interfaces.SessionService;
import com.javaacademy.cinema.service.interfaces.TicketService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/session")
@RestController
@RequiredArgsConstructor
@Tag(name = "Сеанс", description = "Контроллер для работы с сеансами")
public class SessionController {
    private final SessionService sessionService;
    private final TicketService ticketService;
    private final SecurityService securityService;

    @Operation(summary = "Создание сеанса",
            description = "Создание сеанса по id фильма, цене билета и дате",
            responses = {
                    @ApiResponse(responseCode = "201", description = "сеанс создан",
                            content = @Content(schema = @Schema(implementation = SessionDto.class))),
                    @ApiResponse(responseCode = "403",
                            description = "Нет прав доступа, авторизуйтесь как администратор"),
                    @ApiResponse(responseCode = "404",
                            description = "Фильм для создания сессии не найден")
            })
    @PostMapping
    public ResponseEntity<?> createSession(@RequestBody SessionDto sessionDto,
                                           @RequestHeader(value = "user-token") String token) {
        try {
            securityService.checkIsAdmin(token);
            SessionDto newSessionDto = sessionService.save(sessionDto);
            ticketService.createTicketForSession(newSessionDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newSessionDto);
        } catch (ForbiddenAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Список всех сеансов", description = "Возвращает список всех сеансов",
            responses = @ApiResponse(responseCode = "200", description = "Список сеансов",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = SessionDto.class)))))
    @GetMapping
    public ResponseEntity<List<SessionDto>> getAllSession() {
        return ResponseEntity.ok(sessionService.selectAll());
    }

    @Operation(summary = "Показывает все свободные места на сеанс",
            description = "Возвращает список всех свободных мест на сеанс по id",
            responses = {@ApiResponse(responseCode = "200", description = "Список свободных мест",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
                    @ApiResponse(responseCode = "404", description = "Запрашиваемый ресурс не найден")
            })
    @GetMapping("/{sessionId}/free-place")
    public ResponseEntity<List<String>> getFreePlacesForSession(@PathVariable Integer sessionId) {
        List<String> freePlaces = ticketService.selectFreePlace(sessionId);
        if (freePlaces == null || freePlaces.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(freePlaces);
    }
}
