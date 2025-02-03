package com.javaacademy.cinema.service;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.repository.PlaceRepository;
import com.javaacademy.cinema.service.interfaces.ServicePlace;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServicePlaceImpl implements ServicePlace {
    private final PlaceRepository repository;

    @Override
    public List<Place> findAllPlaces() {
        return repository.findAllPlaces();
    }

    public Optional<Place> findById(Integer id) {
        return repository.findById(id);
    }
}
