package com.ramon_silva.projeto_hotel.models;


import org.hibernate.validator.constraints.Length;

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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quartos_de_hotel")
@AllArgsConstructor
@NoArgsConstructor
public class RoomHotelModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelModel hotel;

    @NotBlank
    @Column(name = "numero")
    private String number_room;

    @Enumerated
    @NotNull
    @Column(name = "tipo_de_quarto")
    private TypeRoom type_room;

    @Column(name = "descricao")
    private String description;

    @NotBlank
    @Column(name = "preco")
    private double price;
}
