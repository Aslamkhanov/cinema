package com.javaacademy.cinema.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customApi() {
        Info info = new Info()
                .title("Api для Cinema")
                .description("""
                        Это API для управления афишей кинотеатра. API предоставляет администратору возможность
                        создания кино, сеансы, билеты на сеансы и смотреть проданные билеты.
                        Пользователь имеет доступ к просмотру доступных фильмов, сеансов и покупки билетов."""
                );
        return new OpenAPI().info(info);
    }
}
