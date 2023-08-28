package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDateTime;

import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;



public record Reservation_serviceDto(
     Long id,

     
     ReservationDto reservation,
     
     @NotNull
     ServicesDto servico,
   
     @Future
     LocalDateTime service_hour
) {
   
    
    

}
