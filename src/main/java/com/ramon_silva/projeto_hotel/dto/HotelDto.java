package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;


import com.ramon_silva.projeto_hotel.models.HotelModel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record HotelDto(

    Long id,


    @NotBlank
    @Length(min = 2)
    String name,


    String description,

    @Min(1)
    @Max(5)
    @NotNull
    int classification,

    AddressDto address

    ) {
    
      public HotelDto(HotelModel hotel){
            this(hotel.getId(), hotel.getName(), hotel.getDescription(), hotel.getClassification(),new AddressDto(hotel.getAddress()));
        }



    }