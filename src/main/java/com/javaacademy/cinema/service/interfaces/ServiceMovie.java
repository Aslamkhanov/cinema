package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.CreateMovieDto;

import java.util.List;
import java.util.Optional;

public interface ServiceMovie {
    MovieDto saveMovie(CreateMovieDto createMovieDto);

    List<CreateMovieDto> getAllMovie();

    Optional<CreateMovieDto> findById(Integer id);
}
