package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.service.interfaces.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository repository;

    @Override
    public List<Place> selectAll() {
        return repository.selectAll();
    }

    public Optional<Place> findById(Integer id) {
        return repository.findById(id);
    }
}
