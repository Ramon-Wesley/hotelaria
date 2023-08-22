package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.ramon_silva.projeto_hotel.models.HotelModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record HotelDto(

    Long id,


    @NotBlank
    @Length(min = 2)
    String name,

    @CNPJ
    String cnpj,

    @NotBlank
    @Email
    String email,
    
    @NotBlank
    String phone,
    
    String description,

    @Min(1)
    @Max(5)
    @NotNull
    int classification,

    AddressDto address

    ) {
    
      public HotelDto(HotelModel hotel){
            this(hotel.getId(), hotel.getName(),hotel.getCnpj(),hotel.getEmail(),hotel.getPhone(), hotel.getDescription(), hotel.getClassification(),new AddressDto(hotel.getAddress()));
        }



    }