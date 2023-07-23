package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

public record AddressDto(
 
    Long id,

    @NotBlank
    @Length(min=2)
     String country,
    
    @NotBlank
    @Length(min=2)
     String state,

    @NotBlank
    @Length(min = 10)
     String zapCode,

    @NotBlank
    @Length(min = 2)
     String city,

    @NotBlank
     String neighborhood,
    
    @NotBlank
     String number,

     String complemement
) {
    
}
