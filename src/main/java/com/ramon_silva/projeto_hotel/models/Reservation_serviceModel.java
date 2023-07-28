package com.ramon_silva.projeto_hotel.models;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "reserva_servico")
@Table(name = "reserva_servico")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Reservation_serviceModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    @NotNull
    private ReservationModel reservation;

    @ManyToOne
    @JoinColumn(name = "servico_id")
    @NotNull
    private ServicesModel servico;

    @Future
    @NotNull
    private LocalDateTime service_hour;

}
