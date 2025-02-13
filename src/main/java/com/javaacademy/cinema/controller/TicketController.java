package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketRequestDto;
import com.javaacademy.cinema.dto.TicketResponseDto;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.service.interfaces.SecurityService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket")
@Tag(name = "Билет", description = "Контроллер для работы с билетами")
public class TicketController {
    private final TicketService ticketService;
    private final SecurityService securityService;

    @Operation(summary = "Список купленных билетов",
            description = "Показывает список всех купленных билетов",
            responses = {@ApiResponse(responseCode = "200", description = "Список все купленных билетов",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = TicketDto.class)))),
                    @ApiResponse(responseCode = "403",
                            description = "Нет прав доступа, авторизуйтесь как администратор",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @GetMapping("/saled")
    public ResponseEntity<?> getAllBoughtTickets(@RequestHeader(value = "user-token") String token) {
        try {
            securityService.checkIsAdmin(token);
            return ResponseEntity.ok(ticketService.selectAll());
        } catch (ForbiddenAccessException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "Покупка билета",
            description = "Покупаем билет по id и номеру сеанса",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная покупка билета",
                            content = @Content(schema = @Schema(implementation = TicketResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Билет уже куплен",
                            content = @Content(schema = @Schema(implementation = String.class)))
            })
    @PostMapping("/booking")
    public ResponseEntity<?> bookTicket(@RequestBody TicketRequestDto ticketRequestDto) {
        try {
            TicketResponseDto response = ticketService.bookTicket(ticketRequestDto.getSessionId(),
                    ticketRequestDto.getPlaceName());
            return ResponseEntity.ok(response);
        } catch (TicketAlreadyBookedException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
