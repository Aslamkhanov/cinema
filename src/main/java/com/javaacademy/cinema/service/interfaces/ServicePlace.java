package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.entity.Place;

import java.util.List;
import java.util.Optional;

public interface ServicePlace {
    List<Place> findAllPlaces();

    Optional<Place> findById(Integer id);
}
