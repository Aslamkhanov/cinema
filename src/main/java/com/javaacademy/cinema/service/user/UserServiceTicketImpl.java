package com.javaacademy.cinema.service.user;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.dto.TicketResponse;
import com.javaacademy.cinema.entity.Ticket;
import com.javaacademy.cinema.exception.TicketAlreadyBookedException;
import com.javaacademy.cinema.mapper.MapperTicket;
import com.javaacademy.cinema.repository.TicketRepository;
import com.javaacademy.cinema.service.user.interfaces.UserServiceTicket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceTicketImpl implements UserServiceTicket {
    private final TicketRepository repository;
    private final MapperTicket mapperTicket;

    @Override
    public List<TicketDto> findTicketsBoughtFalse(Integer sessionId) {
        return repository.findTicketsBoughtFalse(sessionId)
                .stream()
                .map(mapperTicket::convertTicketDto)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponse bookTicket(Integer sessionId, String placeName) throws TicketAlreadyBookedException {
            return repository.bookTicket(sessionId, placeName);
    }
}
