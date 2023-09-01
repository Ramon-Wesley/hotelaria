package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDateTime;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Reservation_serviceDto
{
      private Long id;
    
      private ReservationDto reservation;
      
      @NotNull
      private ServicesDto servico;
    
      @Future
      private LocalDateTime service_hour;
   
}
