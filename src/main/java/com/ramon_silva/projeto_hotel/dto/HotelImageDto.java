package com.ramon_silva.projeto_hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class HotelImageDto {

    private Long id;

    @NotBlank
    @NotNull
    private String imageUrl;

    @NotNull
    private HotelDto entity;
}
