package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.MovieDto;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    MovieDto save(MovieDto movieDto);

    List<MovieDto> selectAll();

    Optional<MovieDto> findById(Integer id);
}
