package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.ReservationModel;

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
     @NotNull
     LocalDate checkInDate,

     @Future
     @NotNull
     @Column(name="data_de_checkOut")
     LocalDate checkOutDate,

   
     StatusEnum status,

     
     double total_pay

) {
    public ReservationDto(ReservationModel reservationModel){
     this(reservationModel.getId(),new ClientDto(reservationModel.getClient()),new RoomDto(reservationModel.getRoom()),
     reservationModel.getCheckInDate(),reservationModel.getCheckOutDate(),
      reservationModel.getStatus(), reservationModel.getTotal_pay());
    }
}
