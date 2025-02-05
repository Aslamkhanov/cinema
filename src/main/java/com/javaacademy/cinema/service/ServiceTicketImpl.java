package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponseDto;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.mapper.MapperSession;
import com.javaacademy.cinema.mapper.MapperTicket;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.interfaces.ServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceTicketImpl implements ServiceTicket {
    private final TicketRepository repository;
    private final MapperTicket mapperTicket;
    private final MapperSession mapperSession;
    private final PlaceRepository placeRepository;

    @Override
    public Ticket createTicket(TicketDto ticketDto) {
        Ticket ticket = mapperTicket.convertTicket(ticketDto);
        return repository.createTicket(ticket);
    }

    @Override
    public void statusIsBought(Integer ticketId) {
        try {
            repository.changeStatus(ticketId);
        } catch (TicketAlreadyBookedException | EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> findFreePlaces(Integer sessionId) {
        return repository.findFreePlaces(sessionId);
    }

    @Override
    public List<Ticket> findAllTickets() {
        return repository.findAllTickets();
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

    public List<TicketDto> createTicketForSession(SessionDto sessionDto) throws EntityNotFoundException {
        List<Place> places = placeRepository.findAllPlaces();
        if (places.isEmpty()) {
            throw new RuntimeException("Мест нет");
        }
        List<TicketDto> newTickets = new ArrayList<>();
        Session currentSession = mapperSession.toEntitySession(sessionDto);

        for (Place place : places) {
            Ticket tickets = Ticket.builder()
                    .session(currentSession)
                    .place(place)
                    .isBought(false)
                    .build();
            Ticket ticket = repository.createTicket(tickets);
            newTickets.add(mapperTicket.convertTicketDto(ticket));
        }
        return newTickets;
    }

    @Override
    public TicketResponseDto bookTicket(Integer sessionId, String placeName)
            throws TicketAlreadyBookedException {
        try {
            return repository.bookTicket(sessionId, placeName);
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
