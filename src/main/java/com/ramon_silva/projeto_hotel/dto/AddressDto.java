package com.ramon_silva.projeto_hotel.dto;


import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
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
public class AddressDto{

    private Long id;

    @NotBlank
    @Length(min = 2)
    private String country;

    @NotBlank
    @Length(min = 2)
    private String state;

    @NotBlank
    @Length(min = 9)
    private String zipCode;

    @NotBlank
    @Length(min = 2)
    private String city;

    @NotBlank
    private String neighborhood;

    @NotBlank
    private String number;

    private String complemement;

}
