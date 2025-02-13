package com.javaacademy.cinema.dto;

import com.javaacademy.cinema.entity.Place;
import com.javaacademy.cinema.entity.Session;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Дто билета")
public class TicketDto {
    @Schema(description = "id билета")
    private Integer id;

    @Schema(description = "Сессия")
    private Session session;

    @Schema(description = "Место в билете")
    private Place place;

    @Schema(description = "Куплен билет или нет")
    private Boolean isBought;
}
