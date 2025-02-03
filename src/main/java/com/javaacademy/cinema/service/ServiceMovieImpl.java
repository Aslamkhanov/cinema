package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.dto.CreateMovieDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.mapper.MapperMovie;
import com.javaacademy.cinema.repository.MovieRepository;
import com.javaacademy.cinema.service.interfaces.ServiceMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceMovieImpl implements ServiceMovie {
    private final MovieRepository movieRepository;
    private final MapperMovie mapperMovie;

    @Override
    public MovieDto saveMovie(CreateMovieDto createMovieDto) {
        Movie movie = mapperMovie.convertMovie(createMovieDto);
        movieRepository.createMovie(movie);
        return mapperMovie.convertMovieDto(movie);
    }

    @Override
    public List<CreateMovieDto> getAllMovie() {
        return movieRepository.getAllMovie()
                .stream()
                .map(mapperMovie::convertCreateMovieDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CreateMovieDto> findById(Integer id) {
        return movieRepository.findById(id).map(mapperMovie::convertCreateMovieDto);
    }
}
