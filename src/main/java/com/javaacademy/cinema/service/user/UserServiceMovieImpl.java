package com.javaacademy.cinema.service.user;

import com.javaacademy.cinema.dto.MovieDto;
import com.javaacademy.cinema.mapper.MapperMovie;
import com.javaacademy.cinema.repository.MovieRepository;
import com.javaacademy.cinema.service.user.interfaces.UserServiceMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceMovieImpl implements UserServiceMovie {
    private final MovieRepository repository;
    private final MapperMovie mapperMovie;

    @Override
    public List<MovieDto> getAllMovie() {
        return repository.getAllMovie()
                .stream()
                .map(mapperMovie::convertMovieDto)
                .collect(Collectors.toList());
    }
}
