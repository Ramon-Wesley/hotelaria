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
public class AuthenticatedDto{
    @NotBlank
    @Length(min = 2)
    private String login;

    @NotBlank
    @Length(min = 8)
    private String password;


    
}
