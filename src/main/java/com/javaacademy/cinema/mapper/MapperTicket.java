package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.TicketDto;
import com.javaacademy.cinema.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class MapperTicket {
    public Ticket convertTicket(TicketDto ticketDto) {
        return Ticket.builder()
                .session(ticketDto.getSession())
                .place(ticketDto.getPlace())
                .isBought(ticketDto.getIsBought())
                .build();
    }

    public TicketDto convertTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .session(ticket.getSession())
                .place(ticket.getPlace())
                .isBought(ticket.getIsBought())
                .build();
    }
}
