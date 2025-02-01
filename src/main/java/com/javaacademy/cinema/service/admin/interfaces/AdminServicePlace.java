package com.javaacademy.cinema.service.admin.interfaces;

import com.javaacademy.cinema.entity.Place;

import java.util.List;
import java.util.Optional;

public interface AdminServicePlace {
    List<Place> findAllPlaces();

    Optional<Place> findById(Integer id);
}
