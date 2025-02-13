package com.javaacademy.cinema.config.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SecurityProperty {
    @Value("${app.admin.token}")
    private String token;
}
