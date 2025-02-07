package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponseDto;
import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.entity.TicketResponse;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.mapper.TicketMapper;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository repository;
    private final TicketMapper ticketMapper;
    private final SessionMapper sessionMapper;
    private final PlaceRepository placeRepository;

    @Override
    public Ticket save(TicketDto ticketDto) {
        Ticket ticket = ticketMapper.toEntity(ticketDto);
        return repository.save(ticket);
    }

    @Override
    public void buyTicket(Integer ticketId) {
        repository.buyTicket(ticketId);
    }

    @Override
    public List<String> selectFreePlace(Integer sessionId) {
        return repository.selectFreePlace(sessionId);
    }

    @Override
    public List<Ticket> selectAll() {
        return repository.selectAll();
    }

    @Override
    public List<TicketDto> getSoldTickets(Integer sessionId) {
        return repository.getSoldTickets(sessionId).stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getUnsoldTickets(Integer sessionId) {
        return repository.getUnsoldTickets(sessionId)
                .stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<TicketDto> createTicketForSession(SessionDto sessionDto) {
        List<Place> places = placeRepository.selectAll();
        if (places.isEmpty()) {
            throw new RuntimeException("Мест нет");
        }
        List<TicketDto> newTickets = new ArrayList<>();
        Session currentSession = sessionMapper.toEntity(sessionDto);
        for (Place place : places) {
            Ticket newTicket = Ticket.builder()
                    .session(currentSession)
                    .place(place)
                    .isBought(false)
                    .build();
            Ticket ticket = repository.save(newTicket);
            TicketDto newTicketDto = ticketMapper.toDto(ticket);
            newTicketDto.getSession().setId(currentSession.getId());
            newTickets.add(newTicketDto);
        }
        return newTickets;
    }

    @Override
    public TicketResponseDto bookTicket(Integer sessionId, String placeName) {
        TicketResponse ticketResponse = repository.bookTicket(sessionId, placeName);
        return ticketMapper.toResponseDto(ticketResponse);
    }
}
