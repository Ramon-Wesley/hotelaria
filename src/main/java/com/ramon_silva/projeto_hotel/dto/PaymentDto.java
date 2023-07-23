package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;


import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentDto(
     
     Long id,

     ReservationDto reservation,

     @Enumerated
     @NotNull
     PaymentMethodEnum paymentMethod,

     @Future
     @NotBlank
     LocalDate payment_day,

     @NotBlank
     double total_payment


) {
    
}
