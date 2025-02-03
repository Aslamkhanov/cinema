package com.javaacademy.cinema.service;

import com.javaacademy.cinema.config.ConfigProperty;
import com.javaacademy.cinema.exception.AdminNotFoundException;
import com.javaacademy.cinema.service.interfaces.ConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {
    private final ConfigProperty configProperty;

    public void admin(String token) throws AdminNotFoundException {
        if (!configProperty.getToken().equals(token)) {
            throw new AdminNotFoundException("Нет прав доступа, авторизуйтесь как администратор");
        }
    }
}
