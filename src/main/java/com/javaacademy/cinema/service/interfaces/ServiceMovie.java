package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.MovieDto;

import java.util.List;
import java.util.Optional;

public interface ServiceMovie {
    MovieDto saveMovie(MovieDto MovieDto);

    List<MovieDto> getAllMovie();

    Optional<MovieDto> findById(Integer id);
}
