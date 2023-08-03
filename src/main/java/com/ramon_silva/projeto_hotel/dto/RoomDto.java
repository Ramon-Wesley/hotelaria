package com.ramon_silva.projeto_hotel.dto;
import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;
import com.ramon_silva.projeto_hotel.models.RoomModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RoomDto(

     Long id,

     HotelDto hotel,

     @NotBlank
     String number_room,


     @NotNull
     TypeRoomEnum type_room,

    
     String description,

     @NotNull
     @Positive
     Double price


) {
    public RoomDto(RoomModel roomModel){
     this(roomModel.getId(),new HotelDto(roomModel.getHotel()), roomModel.getNumber_room(), roomModel.getType_room(),roomModel.getDescription(), roomModel.getPrice());
    }
}
