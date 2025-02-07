package com.javaacademy.cinema.service.interfaces;

import com.javaacademy.cinema.exception.ForbiddenAccessException;

public interface ConfigService {
    public void checkIsAdmin(String token) throws ForbiddenAccessException;
}
