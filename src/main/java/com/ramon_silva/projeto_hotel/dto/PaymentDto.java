package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class PaymentDto {
    private Long id;

    @NotNull
    private ReservationDto reservation;

    @NotNull
    private PaymentMethodEnum paymentMethod;

    @NotNull
    private LocalDate payment_day = LocalDate.now();

    @NotNull
    private StatusEnum status;

    private double total_payment;
}
