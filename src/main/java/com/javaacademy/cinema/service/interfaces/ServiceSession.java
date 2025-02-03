package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;

import java.util.List;
import java.util.Optional;

public interface ServiceSession {

    Session createSession(SessionDto sessionDto);

    List<SessionDto> getAllSession();

    Optional<Session> findById(Integer id);
}
