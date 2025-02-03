package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponse;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.mapper.MapperTicket;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.interfaces.ServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceTicketImpl implements ServiceTicket {
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
    public List<TicketDto> findTicketsBoughtTrue(Integer sessionId) {
        return repository.findTicketsBoughtTrue(sessionId).stream()
                .map(mapperTicket::convertTicketDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<TicketDto> findTicketsBoughtFalse(Integer sessionId) {
        return repository.findTicketsBoughtFalse(sessionId)
                .stream()
                .map(mapperTicket::convertTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public void createTicketsForSession(Integer sessionId, List<Place> places) {
        repository.createTicketsForSession(sessionId, places);
    }

    @Override
    public TicketResponse bookTicket(Integer sessionId, String placeName) throws TicketAlreadyBookedException {
        return repository.bookTicket(sessionId, placeName);
    }
}
