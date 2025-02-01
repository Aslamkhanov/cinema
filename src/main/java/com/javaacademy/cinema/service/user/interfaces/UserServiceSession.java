package com.javaacademy.cinema.service.user.interfaces;

import com.javaacademy.cinema.dto.SessionDto;
import com.javaacademy.cinema.entity.Session;

import java.util.List;

public interface UserServiceSession {
    List<SessionDto> getAllSession();
}
