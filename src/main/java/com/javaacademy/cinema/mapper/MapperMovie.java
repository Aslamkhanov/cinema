package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MapperMovie {
    public Movie convertMovie(MovieDto movieDto) {
        return Movie.builder()
                .name(movieDto.getName())
                .description(movieDto.getDescription())
                .build();
    }

    public MovieDto convertMovieDto(Movie movie) {
        return MovieDto.builder()
                .name(movie.getName())
                .description(movie.getDescription())
                .build();
    }
}
