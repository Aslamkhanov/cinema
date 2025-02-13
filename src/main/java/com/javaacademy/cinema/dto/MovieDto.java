package com.javaacademy.cinema.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Дто фильма")
public class MovieDto {
    @Schema(description = "id фильма")
    private Integer id;

    @Schema(description = "название фильма")
    private String name;

    @Schema(description = "описание фильма")
    private String description;
}
