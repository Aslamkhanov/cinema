package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionMapper {
    private final MovieRepository movieRepository;

    public Session toEntity(SessionDto sessionDto) {
        Integer movieId = sessionDto.getMovieId();
        Movie movie = movieRepository.findById(sessionDto.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Билет с id " + movieId + " не найден"));
        return Session.builder()
                .id(sessionDto.getId())
                .movie(movie)
                .dateTime(sessionDto.getDateTime())
                .price(sessionDto.getPrice())
                .build();
    }

    public SessionDto toDto(Session session) {
        return SessionDto.builder()
                .id(session.getId())
                .movieId(session.getMovie().getId())
                .dateTime(session.getDateTime())
                .price(session.getPrice())
                .build();
    }
}
