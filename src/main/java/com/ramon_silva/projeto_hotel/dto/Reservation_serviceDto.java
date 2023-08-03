package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDateTime;

import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;



public record Reservation_serviceDto(
     Long id,
     ReservationModel reservation,
     ServicesModel servico,
   
     LocalDateTime service_hour
) {
   
    public Reservation_serviceDto(Reservation_serviceModel reservation_serviceModel){
        this(reservation_serviceModel.getId(),reservation_serviceModel.getReservation(), reservation_serviceModel.getServico(), reservation_serviceModel.getService_hour());
    }
    

}
