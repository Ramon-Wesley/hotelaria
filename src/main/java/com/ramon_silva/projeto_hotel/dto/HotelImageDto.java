package com.ramon_silva.projeto_hotel.dto;

import com.ramon_silva.projeto_hotel.models.HotelModel;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class HotelImageDto {

    private Long id;
    
    @NotBlank
    @NotNull
    private String imageUrl;

    @NotNull
    private HotelDto hotel;
}
