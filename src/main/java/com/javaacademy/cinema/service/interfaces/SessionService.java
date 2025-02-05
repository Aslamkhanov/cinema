package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.GetSessionDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface SessionService {
    SessionDto save(SessionDto sessionDto) throws EntityNotFoundException;

    List<GetSessionDto> selectAll();

    Optional<Session> findById(Integer id);
}
