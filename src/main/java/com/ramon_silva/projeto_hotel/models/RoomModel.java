package com.ramon_silva.projeto_hotel.models;


import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quartos_de_hotel")
@AllArgsConstructor
@NoArgsConstructor
public class RoomModel {
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
    private TypeRoomEnum type_room;

    @Column(name = "descricao")
    private String description;

    @NotBlank
    @Column(name = "preco")
    private double price;
}
