package com.javaacademy.cinema.service;

import com.javaacademy.cinema.config.security.SecurityProperty;
import com.javaacademy.cinema.exception.ForbiddenAccessException;
import com.javaacademy.cinema.service.interfaces.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final SecurityProperty securityProperty;

    public void checkIsAdmin(String token) throws ForbiddenAccessException {
        if (!securityProperty.getToken().equals(token)) {
            throw new ForbiddenAccessException();
        }
    }
}
