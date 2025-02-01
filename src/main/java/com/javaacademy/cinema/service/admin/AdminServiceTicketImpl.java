package com.javaacademy.cinema.service.admin;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.mapper.MapperTicket;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.admin.interfaces.AdminServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminServiceTicketImpl implements AdminServiceTicket {
    private final TicketRepository repository;
    private final MapperTicket mapperTicket;

    @Override
    public Ticket createTicket(TicketDto ticketDto) {
        Ticket ticket = mapperTicket.convertTicket(ticketDto);
        return repository.createTicket(ticket);
    }

    @Override
    public void statusIsBought(Integer ticketId) {
        try {
            repository.statusIsBought(ticketId);
        } catch (TicketAlreadyBookedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ticket> findTicketsBoughtTrue(Integer sessionId) {
        return repository.findTicketsBoughtTrue(sessionId);
    }

    @Override
    public List<Ticket> findTicketsBoughtFalse(Integer sessionId) {
        return repository.findTicketsBoughtFalse(sessionId);
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public void createTicketsForSession(Integer sessionId, List<Place> places) {
        repository.createTicketsForSession(sessionId, places);
    }
}
