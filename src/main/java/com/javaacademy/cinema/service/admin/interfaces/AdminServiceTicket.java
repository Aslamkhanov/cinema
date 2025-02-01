package com.javaacademy.cinema.service.admin.interfaces;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface AdminServiceTicket {
    Ticket createTicket(TicketDto ticketDto);

    void statusIsBought(Integer ticketId);

    List<Ticket> findTicketsBoughtTrue(Integer sessionId);

    List<Ticket> findTicketsBoughtFalse(Integer sessionId);

    Optional<Ticket> findById(Integer id);

    void createTicketsForSession(Integer sessionId, List<Place> places);
}
