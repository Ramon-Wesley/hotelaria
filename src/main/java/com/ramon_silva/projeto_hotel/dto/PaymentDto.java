package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.PaymentModel;


import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record PaymentDto(
     
     Long id,

     
     ReservationDto reservation,

     @Enumerated
     @NotNull
     PaymentMethodEnum paymentMethod,

     @Future
     @NotNull
     LocalDate payment_day,

     @Enumerated
     StatusEnum status,

     
     double total_payment


) {
    public PaymentDto(PaymentModel paymentModel){
     this(paymentModel.getId(),new ReservationDto(paymentModel.getReservation()), paymentModel.getPaymentMethod(), 
     paymentModel.getPayment_day(), paymentModel.getStatus(),paymentModel.getTotal_payment());
    }
}
