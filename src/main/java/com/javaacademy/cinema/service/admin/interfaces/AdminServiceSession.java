package com.javaacademy.cinema.service.admin.interfaces;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;

import java.util.List;
import java.util.Optional;

public interface AdminServiceSession {

    Session createSession(SessionDto sessionDto);

    List<Session> getAllSession();

    Optional<Session> findById(Integer id);
}
