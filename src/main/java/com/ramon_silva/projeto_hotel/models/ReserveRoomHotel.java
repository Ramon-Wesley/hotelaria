package com.ramon_silva.projeto_hotel.models;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Reservas")
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRoomHotel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="cliente_id")
    private ClientModel client;

    @ManyToOne
    @JoinColumn(name="quarto_id")
    private ClientModel room;

    @Future
    @NotBlank
    @Column(name="data_de_checkIn")
    private LocalDate checkInDate;

    @Future
    @NotBlank
    @Column(name="data_de_checkOut")
    private LocalDate checkOutDate;

    @Enumerated
    @NotNull
    private StatusReservation status;

    @Column(name = "total_a_pagar")
    @NotBlank
    private double total_pay;
}
