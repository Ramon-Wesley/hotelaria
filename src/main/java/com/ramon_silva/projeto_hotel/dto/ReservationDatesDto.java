package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ReservationDatesDto(
     @Future
     @NotNull
     LocalDate checkInDate,
     
     @Future
     @NotNull
     LocalDate checkOutDate

) {
    
}
