package com.ramon_silva.projeto_hotel.models;


import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pagamentos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentModel {

    public PaymentModel(PaymentDto paymentDto){
    this.reservation=new ReservationModel(paymentDto.reservation());
    this.paymentMethod=paymentDto.paymentMethod();
    this.payment_day=paymentDto.payment_day();
    this.status=paymentDto.status();
    this.total_payment=paymentDto.total_payment();
    }
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "reserva_id")
    private ReservationModel reservation;

    
    @Enumerated
    @NotNull
    @Column(name = "metodo_de_pagamento")
    private PaymentMethodEnum paymentMethod;

    @NotNull
    @Future
    @NotBlank
    @Column(name="data_do_pagamento")
    private LocalDate payment_day;

    @Enumerated
    @NotNull
    private StatusEnum status;

    @NotNull
    @NotBlank
    @Column(name="total_do_pagamento")
    private double total_payment;
}
