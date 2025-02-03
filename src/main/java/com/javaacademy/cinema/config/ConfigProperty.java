package com.javaacademy.cinema.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class ConfigProperty {
    @Value("${app.admin.token}")
    private String token;
}
