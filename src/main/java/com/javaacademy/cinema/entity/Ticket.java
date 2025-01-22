package com.javaacademy.cinema.entity;

import lombok.Data;

@Data
public class Ticket {
    private Integer id;
    private Session sessionId;
    private Place placeId;
    private String boughtOrNot;
}
