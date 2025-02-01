package com.javaacademy.cinema.service.user;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.mapper.MapperSession;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.service.user.interfaces.UserServiceSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceSessionImpl implements UserServiceSession {
    private final SessionRepository repository;
    private final MapperSession mapperSession;

    @Override
    public List<SessionDto> getAllSession() {
        return repository.getAllSession()
                .stream()
                .map(mapperSession::convertSessionDto)
                .collect(Collectors.toList());
    }
}
