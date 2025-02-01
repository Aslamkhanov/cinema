package com.javaacademy.cinema.service.user.interfaces;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;

import java.util.List;

public interface UserServiceMovie {
    List<MovieDto> getAllMovie();
}
