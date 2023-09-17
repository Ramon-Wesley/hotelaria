package com.ramon_silva.projeto_hotel.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "quarto_imagens")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RoomImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank

    @NotNull
    @NotBlank
    @Column(name="tipo")
    private String type;

    @NotBlank
    @NotNull
    @Column(name="imagem_url")
    private String imageUrl;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "quarto_id")
    private RoomModel room;

}
