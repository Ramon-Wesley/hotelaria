package com.ramon_silva.projeto_hotel.dto;

import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;

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
public class PaymentMethodDto {

    private long reservation_id;

    @NotNull
    private PaymentMethodEnum paymentMethodEnum;
}
