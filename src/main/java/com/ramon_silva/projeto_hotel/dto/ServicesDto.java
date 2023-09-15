package com.ramon_silva.projeto_hotel.dto;

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
public class ServicesDto{
private Long id;
@NotBlank
@NotNull
private String name;
@NotBlank
@NotNull
private String type_service;
@NotBlank
@NotNull
private Boolean active;
@NotBlank
@NotNull
private Double price;

    

}
