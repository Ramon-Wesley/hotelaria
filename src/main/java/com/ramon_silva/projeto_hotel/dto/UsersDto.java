package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.enums.UsersEnum;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

public record UsersDto(
    
    Long id,

    @NotBlank
    @Length(min = 2)
    String login,

    @NotBlank
    @Length(min = 8)
    String password,

    @Enumerated
    UsersEnum typeUser


) {
    
}
