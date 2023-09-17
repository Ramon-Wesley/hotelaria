package com.ramon_silva.projeto_hotel.models;


import java.util.ArrayList;
import java.util.List;

import com.ramon_silva.projeto_hotel.dto.RoomDto;
import com.ramon_silva.projeto_hotel.enums.TypeRoomEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id",nullable = false)
    private HotelModel hotel;

    @NotBlank
    @NotNull
    @Column(name = "numero")
    private String number_room;


    @NotNull
    @Column(name = "tipo_de_quarto")
    private TypeRoomEnum type_room;

    @Column(name = "descricao")
    private String description;


    @Column(name = "ativo")
    private Boolean active=true;
    @NotNull
    @Positive
    @Column(name = "preco")
    private Double price;

    @OneToMany(mappedBy = "room",orphanRemoval = true,cascade = CascadeType.ALL)
     private List<RoomImage> roomImages=new ArrayList<>();

}
