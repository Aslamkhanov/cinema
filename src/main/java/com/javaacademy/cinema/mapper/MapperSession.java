package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.GetSessionDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperSession {
    private final MovieRepository movieRepository;

    public Session toEntitySession(SessionDto sessionDto) throws EntityNotFoundException {
        Integer movieId = sessionDto.getMovieId();
        Movie movie = movieRepository.findById(sessionDto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Билет с id " + movieId + " не найден"));
        return Session.builder()
                .movie(movie)
                .dateTime(sessionDto.getDate())
                .price(sessionDto.getPrice())
                .build();
    }

    public SessionDto toSessionDto(Session session) {
        return SessionDto.builder()
                .id(session.getId())
                .movieId(session.getMovie().getId())
                .date(session.getDateTime())
                .price(session.getPrice())
                .build();
    }

    public GetSessionDto toGetSessionDto(Session session) {
        return GetSessionDto.builder()
                .id(session.getId())
                .movieId(session.getMovie().getName())
                .date(session.getDateTime())
                .price(session.getPrice())
                .build();
    }
}
