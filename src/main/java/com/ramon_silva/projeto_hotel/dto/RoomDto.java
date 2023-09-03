package com.ramon_silva.projeto_hotel.dto;
import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;
import com.ramon_silva.projeto_hotel.models.RoomModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class RoomDto{

    private Long id;

     @NotNull
     private HotelDto hotel;

     @NotBlank
     private String number_room;


     @NotNull
     private TypeRoomEnum type_room;

    
     private String description;

     @NotNull
     @Positive
     private Double price;



    
}
