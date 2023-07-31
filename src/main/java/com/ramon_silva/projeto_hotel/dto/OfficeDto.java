package com.ramon_silva.projeto_hotel.dto;

import com.ramon_silva.projeto_hotel.models.OfficesModel;

public record OfficeDto(
long id,
String name
) {
    
    public OfficeDto(OfficesModel office){
    this(office.getId(),office.getName());
    }
}
