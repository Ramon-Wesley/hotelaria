package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.bouncycastle.asn1.cms.RsaKemParameters;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record ReservationDto(

     Long id,
    
     @NotNull
     ClientDto client,

     @NotNull
     RoomDto room,

     @Future
     @NotNull
     LocalDate checkInDate,

     @Future
     @NotNull
     LocalDate checkOutDate,
   
     StatusEnum status,

     double total_pay,
    
     Set<Reservation_serviceDto> reservation_service


) {
}
