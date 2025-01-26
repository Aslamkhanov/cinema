package com.javaacademy.cinema.entity;

import lombok.Data;

@Data
public class Ticket {
    private Integer id;
    private Session session;
    private Place place;
    private Boolean isBought;
}
