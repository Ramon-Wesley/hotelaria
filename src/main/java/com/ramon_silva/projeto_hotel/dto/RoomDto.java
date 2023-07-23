package com.ramon_silva.projeto_hotel.dto;

import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;
import com.ramon_silva.projeto_hotel.models.HotelModel;

import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RoomDto(

     Long id,

     HotelDto hotel,

     @NotBlank
     @Column(name = "numero")
     String number_room,

     @Enumerated
     @NotNull
     @Column(name = "tipo_de_quarto")
     TypeRoomEnum type_room,

     @Column(name = "descricao")
     String description,

     @NotBlank
     @Column(name = "preco")
     double price


) {
    
}
