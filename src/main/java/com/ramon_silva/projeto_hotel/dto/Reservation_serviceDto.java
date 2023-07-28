package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDateTime;

import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;

import jakarta.validation.constraints.NotNull;



public record Reservation_serviceDto(
     ReservationModel reservation,
     ServicesModel servico,
     @NotNull
     LocalDateTime service_hour
) {
   

}
