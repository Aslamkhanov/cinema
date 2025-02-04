package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponseDto;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Ticket;
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

    void createTicketsForSession(Integer sessionId, List<Place> places);

    TicketResponseDto bookTicket(Integer sessionId, String placeName) throws TicketAlreadyBookedException;
}
