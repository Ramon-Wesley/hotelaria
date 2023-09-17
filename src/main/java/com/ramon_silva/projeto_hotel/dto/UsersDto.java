package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.enums.UsersEnum;

import jakarta.persistence.Enumerated;
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
public class UsersDto{
    
    private Long id;

    @NotBlank
    @Length(min = 2)
    private String login;

    @NotBlank
    @Length(min = 8)
    private String password;

    @Enumerated
    @NotNull
    private UsersEnum role;

    
}
