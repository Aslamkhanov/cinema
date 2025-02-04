package com.javaacademy.cinema.controller;

import com.javaacademy.cinema.dto.TicketRequestDto;
import com.javaacademy.cinema.dto.TicketResponseDto;
import com.javaacademy.cinema.exception.AdminNotFoundException;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.service.interfaces.ConfigService;
import com.javaacademy.cinema.service.interfaces.ServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket")
public class TicketController {
    private final ServiceTicket serviceTicket;
    private final ConfigService configService;

    @GetMapping("/saled")
    public ResponseEntity<?> getAllBoughtTickets(@RequestHeader(value = "user-token") String token) {
        try {
            configService.admin(token);
            return ResponseEntity.ok(serviceTicket.findAllTickets());
        } catch (AdminNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping("/booking")
    public ResponseEntity<?> bookTicket(@RequestBody TicketRequestDto ticketRequestDto) {
        try {
            TicketResponseDto response = serviceTicket.bookTicket(ticketRequestDto.getSessionId(),
                    ticketRequestDto.getPlaceName());
            return ResponseEntity.ok(response);
        } catch (TicketAlreadyBookedException e) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(e.getMessage());
        }
    }
}
