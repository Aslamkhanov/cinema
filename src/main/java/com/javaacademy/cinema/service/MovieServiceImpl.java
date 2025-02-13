package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.mapper.MovieMapper;
import com.javaacademy.cinema.repository.MovieRepository;
import com.javaacademy.cinema.service.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    @Override
    public MovieDto save(MovieDto movieDto) {
        Movie movie = movieMapper.toEntity(movieDto);
        Movie saveMovie = movieRepository.save(movie);
        return movieMapper.toDto(saveMovie);
    }

    @Override
    public List<MovieDto> selectAll() {
        return movieRepository.selectAll()
                .stream()
                .map(movieMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieDto> findById(Integer id) {
        return movieRepository.findById(id).map(movieMapper::toDto);
    }
}
