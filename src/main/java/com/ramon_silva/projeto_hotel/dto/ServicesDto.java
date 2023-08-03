package com.ramon_silva.projeto_hotel.dto;

import com.ramon_silva.projeto_hotel.models.ServicesModel;

public record ServicesDto(
Long id,
String name,
String type_service,
Double price
) {
    public ServicesDto(ServicesModel service){
        this(service.getId(),service.getName(), service.getType_service(), service.getPrice());
    }
}
