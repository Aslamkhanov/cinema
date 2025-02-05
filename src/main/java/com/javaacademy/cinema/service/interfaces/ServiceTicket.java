package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponseDto;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;

import java.util.List;
import java.util.Optional;

public interface ServiceTicket {
    Ticket createTicket(TicketDto ticketDto);

    void statusIsBought(Integer ticketId);

    List<String> findFreePlaces(Integer sessionId);

    List<Ticket> findAllTickets();

    List<TicketDto> findTicketsBoughtTrue(Integer sessionId);

    List<TicketDto> findTicketsBoughtFalse(Integer sessionId);

    Optional<Ticket> findById(Integer id);

    List<TicketDto> createTicketForSession(SessionDto sessionDto) throws EntityNotFoundException;

    TicketResponseDto bookTicket(Integer sessionId, String placeName) throws TicketAlreadyBookedException;
}
