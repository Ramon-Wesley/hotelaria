package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDateTime;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
public class Reservation_serviceDto
{
      private Long id;
    
      private ReservationDto reservation;
      
      @NotNull
      private ServicesDto servico;
    
      @Future
      private LocalDateTime service_hour;
   
}
