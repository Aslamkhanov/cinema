package com.javaacademy.cinema.service.admin;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.entity.Movie;
import com.javaacademy.cinema.mapper.MapperMovie;
import com.javaacademy.cinema.repository.MovieRepository;
import com.javaacademy.cinema.service.admin.interfaces.AdminServiceMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceMovieMovieImpl implements AdminServiceMovie {
    private final MovieRepository movieRepository;
    private final MapperMovie mapperMovie;

    @Override
    public Movie saveMovie(MovieDto movieDto) {
        Movie movie = mapperMovie.convertMovie(movieDto);
        return movieRepository.createMovie(movie);
    }

    @Override
    public List<Movie> getAllMovie() {
        return movieRepository.getAllMovie();
    }
    @Override
    public Optional<Movie> findById(Integer id) {
        return movieRepository.findById(id);
    }
}
