package com.javaacademy.cinema.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date_time")
    private LocalDateTime dateTime;
}
