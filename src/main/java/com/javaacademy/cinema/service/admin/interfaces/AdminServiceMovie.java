package com.javaacademy.cinema.service.admin.interfaces;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;

import java.util.List;
import java.util.Optional;

public interface AdminServiceMovie {
    Movie saveMovie(MovieDto movieDto);

    List<Movie> getAllMovie();

    Optional<Movie> findById(Integer id);
}
