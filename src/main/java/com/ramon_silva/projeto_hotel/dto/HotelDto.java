package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;



import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record HotelDto(

    Long id,

    @NotBlank
    @Length(min = 2)
    String name,

  
    String description,
    
    @Min(1)
    @Max(5)
    @NotBlank
    int classification,

    AddressDto address

) {
    
}
