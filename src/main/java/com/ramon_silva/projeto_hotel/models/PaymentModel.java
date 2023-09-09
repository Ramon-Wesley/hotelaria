package com.ramon_silva.projeto_hotel.models;

import java.time.LocalDate;

import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reserva_id")
    private ReservationModel reservation;

    @Enumerated
    @NotNull
    @Column(name = "metodo_de_pagamento")
    private PaymentMethodEnum paymentMethod;

    @Column(name = "data_do_pagamento")
    private LocalDate payment_day = LocalDate.now();

    @Enumerated
    @NotNull
    private StatusEnum status;

    @NotNull
    @Column(name = "total_do_pagamento")
    private double total_payment;
}
