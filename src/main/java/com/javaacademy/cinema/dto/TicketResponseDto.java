package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TicketResponseDto {
    @JsonProperty("ticket_id")
    private Integer ticketId;
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("movie_name")
    private String movieName;
    private LocalDateTime date;
}
