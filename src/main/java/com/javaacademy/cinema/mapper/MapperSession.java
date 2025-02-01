package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import org.springframework.stereotype.Component;

@Component
public class MapperSession {
    public Session convertSession(SessionDto sessionDto) {
        return Session.builder().movie(sessionDto.getMovie())
                .dateTime(sessionDto.getDate())
                .price(sessionDto.getPrice())
                .build();
    }

    public SessionDto convertSessionDto(Session session) {
        return SessionDto.builder()
                .id(session.getId())
                .movie(session.getMovie())
                .date(session.getDateTime())
                .price(session.getPrice())
                .build();
    }
}
