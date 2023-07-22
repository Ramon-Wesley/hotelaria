package com.ramon_silva.projeto_hotel.models;


import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.dto.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.dto.StatusReservation;
import com.ramon_silva.projeto_hotel.dto.TypeRoom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pagamentos")
@AllArgsConstructor
@NoArgsConstructor
public class PaymentModel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reserva_id")
    private ReserveRoomHotel reservation;

    @Enumerated
    @NotNull
    @Column(name = "metodo_de_pagamento")
    private PaymentMethodEnum paymentMethod;

    @Future
    @NotBlank
    @Column(name="data_do_pagamento")
    private LocalDate payment_day;

    @NotBlank
    @Column(name="total_do_pagamento")
    private double total_payment;
}
