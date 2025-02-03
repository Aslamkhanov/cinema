package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.mapper.MapperSession;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.service.interfaces.ServiceSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ServiceSessionImpl implements ServiceSession {
    private final SessionRepository repository;
    private final MapperSession mapperSession;

    @Override
    public Session createSession(SessionDto sessionDto) {
        Session session = mapperSession.convertSession(sessionDto);
        return repository.createSession(session);
    }

    @Override
    public List<SessionDto> getAllSession() {
        return repository.getAllSession()
                .stream()
                .map(mapperSession::convertSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Session> findById(Integer id) {
        return repository.findById(id);
    }
}
