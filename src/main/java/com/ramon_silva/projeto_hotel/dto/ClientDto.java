package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientDto(
    
    Long id,
    
    @NotBlank
    @Length(min = 2)
    String name,
    
    @NotBlank
    @Email
    String email,
    
    @NotBlank
    String phone,

    AddressDto address

) {
    
}
