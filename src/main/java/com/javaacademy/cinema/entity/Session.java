package com.javaacademy.cinema.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Session {
    private Integer id;
    private Movie movie;
    private LocalDateTime dateTime;
    private BigDecimal price;
}
