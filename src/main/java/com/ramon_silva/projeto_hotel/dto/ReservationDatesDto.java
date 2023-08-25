package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.StatusEnum;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ReservationDatesDto(
     @Future
     @NotNull
     LocalDate checkInDate,
     
     @Future
     @NotNull
     LocalDate checkOutDate,

     @NotNull
     StatusEnum status

) {
    
}
