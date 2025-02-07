package com.javaacademy.cinema.service;

import com.javaacademy.cinema.config.ConfigProperty;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigProperty configProperty;

    public void checkIsAdmin(String token) throws ForbiddenAccessException {
        if (!configProperty.getToken().equals(token)) {
            throw new ForbiddenAccessException("Нет прав доступа, авторизуйтесь как администратор");
        }
    }
}
