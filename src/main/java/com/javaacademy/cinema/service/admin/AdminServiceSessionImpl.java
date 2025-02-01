package com.javaacademy.cinema.service.admin;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.mapper.MapperSession;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.service.admin.interfaces.AdminServiceSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminServiceSessionImpl implements AdminServiceSession {
    private final SessionRepository repository;
    private final MapperSession mapperSession;

    @Override
    public Session createSession(SessionDto sessionDto) {
        Session session = mapperSession.convertSession(sessionDto);
        return repository.createSession(session);
    }

    @Override
    public List<Session> getAllSession() {
        return repository.getAllSession();
    }

    @Override
    public Optional<Session> findById(Integer id) {
        return repository.findById(id);
    }
}
