package com.javaacademy.cinema.service.user.interfaces;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponse;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;

import java.util.List;

public interface UserServiceTicket {
    List<TicketDto> findTicketsBoughtFalse(Integer sessionId);
    TicketResponse bookTicket(Integer sessionId, String placeName) throws TicketAlreadyBookedException;
}
