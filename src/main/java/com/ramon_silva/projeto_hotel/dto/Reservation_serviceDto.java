package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDateTime;

import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;



public record Reservation_serviceDto(
     Long id,

     @NotNull
     ReservationDto reservation,
     
     @NotNull
     ServicesDto servico,
   
     @Future
     LocalDateTime service_hour
) {
   
    public Reservation_serviceDto(Reservation_serviceModel reservation_serviceModel){
        this(reservation_serviceModel.getId(),new ReservationDto(reservation_serviceModel.getReservation()), new ServicesDto(reservation_serviceModel.getServico()), reservation_serviceModel.getService_hour());
    }
    

}
