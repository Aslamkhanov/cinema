package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.exception.AdminNotFoundException;

public interface ConfigService {
    public void admin(String token) throws AdminNotFoundException;
}
