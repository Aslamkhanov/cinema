package com.javaacademy.cinema.dto;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketDto {
    private Integer id;
    private Session session;
    private Place place;
    private Boolean isBought;
}
