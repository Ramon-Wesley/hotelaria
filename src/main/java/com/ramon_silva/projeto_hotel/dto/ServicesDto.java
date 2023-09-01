package com.ramon_silva.projeto_hotel.dto;

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
private String name;
private String type_service;
private Double price;

    

}
