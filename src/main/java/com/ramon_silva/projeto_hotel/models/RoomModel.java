package com.ramon_silva.projeto_hotel.models;


import com.ramon_silva.projeto_hotel.dto.RoomDto;
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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "quartos_de_hotel")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RoomModel {


    public RoomModel(RoomDto roomDto){
        this.number_room=roomDto.number_room();
        this.type_room=roomDto.type_room();
        this.description=roomDto.description();
        this.price=roomDto.price();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelModel hotel;

    @NotBlank
    @NotNull
    @Column(name = "numero")
    private String number_room;

    @Enumerated
    @NotNull
    @Column(name = "tipo_de_quarto")
    private TypeRoomEnum type_room;

    @Column(name = "descricao")
    private String description;

    @NotNull
    @Column(name = "preco")
    private double price;
}
