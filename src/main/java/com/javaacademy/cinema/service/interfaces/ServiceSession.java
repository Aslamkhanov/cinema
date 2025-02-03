package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.GetSessionDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ServiceSession {

    Session createSession(SessionDto sessionDto) throws EntityNotFoundException;

    List<GetSessionDto> getAllSession();

    Optional<Session> findById(Integer id);
}
