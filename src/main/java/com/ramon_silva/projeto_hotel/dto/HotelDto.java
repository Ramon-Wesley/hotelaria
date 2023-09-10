package com.ramon_silva.projeto_hotel.dto;

import java.util.List;

import java.util.Map;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.ramon_silva.projeto_hotel.models.HotelModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class HotelDto{

    private Long id;


    @NotBlank
    @Length(min = 2)
    private String name;

    @CNPJ
    private String cnpj;

    @NotBlank
    @Email
    private String email;
    
   @NotBlank
    private String phone;
    
    private String description;

    @Min(1)
    @Max(5)
    @NotNull
    private int classification;

    private AddressDto address;

    private List<HotelImageDto> hotelImageDtos;

    }