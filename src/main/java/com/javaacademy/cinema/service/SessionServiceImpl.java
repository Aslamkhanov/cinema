package com.javaacademy.cinema.service;

import com.javaacademy.cinema.dto.GetSessionDto;
import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;
import com.javaacademy.cinema.exception.EntityNotFoundException;
import com.javaacademy.cinema.mapper.SessionMapper;
import com.javaacademy.cinema.repository.SessionRepository;
import com.javaacademy.cinema.service.interfaces.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    private final SessionRepository repository;
    private final SessionMapper sessionMapper;

    @Override
    public SessionDto save(SessionDto sessionDto) throws EntityNotFoundException {
        Session session = sessionMapper.toEntity(sessionDto);
        Session newSession = repository.save(session);
        return sessionMapper.toDto(newSession);
    }

    @Override
    public List<GetSessionDto> selectAll() {
        return repository.selectAll()
                .stream()
                .map(sessionMapper::toGetSessionDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Session> findById(Integer id) {
        return repository.findById(id);
    }
}
