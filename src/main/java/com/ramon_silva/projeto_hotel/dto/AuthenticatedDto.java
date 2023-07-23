package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record AuthenticatedDto(
    @NotBlank
    @Length(min = 2)
    String login,

    @NotBlank
    @Length(min = 8)
    String password

) {
    
}
