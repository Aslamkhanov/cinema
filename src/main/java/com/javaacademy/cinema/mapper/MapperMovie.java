package com.javaacademy.cinema.mapper;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;
import org.springframework.stereotype.Component;

@Component
public class MapperMovie {
    public Movie convertMovie(MovieDto MovieDto) {
        return Movie.builder()
                .name(MovieDto.getName())
                .description(MovieDto.getDescription())
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
