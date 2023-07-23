package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.StatusReservationEnum;


import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationDto(

     Long id,
    
     ClientDto client,

     RoomDto room,

     @Future
     @NotBlank
     LocalDate checkInDate,

     @Future
     @NotBlank
     @Column(name="data_de_checkOut")
     LocalDate checkOutDate,

     @Enumerated
     @NotNull
     StatusReservationEnum status,

     @NotBlank
     double total_pay

) {
    
}
