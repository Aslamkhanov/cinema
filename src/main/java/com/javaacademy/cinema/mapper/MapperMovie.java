package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.CreateMovieDto;
import com.javaacademy.cinema.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MapperMovie {
    public Movie convertMovie(CreateMovieDto createMovieDto) {
        return Movie.builder()
                .name(createMovieDto.getName())
                .description(createMovieDto.getDescription())
                .build();
    }

    public CreateMovieDto convertCreateMovieDto(Movie movie) {
        return CreateMovieDto.builder()
                .name(movie.getName())
                .description(movie.getDescription())
                .build();
    }
    public MovieDto convertMovieDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .name(movie.getName())
                .description(movie.getDescription())
                .build();
    }
}
