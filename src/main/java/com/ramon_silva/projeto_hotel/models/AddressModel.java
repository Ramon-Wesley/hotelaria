package com.ramon_silva.projeto_hotel.models;


import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "enderecos")
@AllArgsConstructor
@NoArgsConstructor
public class AddressModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "pais")
    @Length(min=2)
    private String country;

    @NotBlank
    @Column(name = "estado")
    @Length(min=2)
    private String state;

    @NotBlank
    @Column(name = "CEP")
    @Length(min = 10)
    private String zapCode;

    @NotBlank
    @Column(name = "cidade")
    @Length(min = 2)
    private String city;

    @NotBlank
    @Column(name = "bairro")
    private String neighborhood;
    
    @NotBlank
    @Column(name = "numero")
    private String number;

    @Column(name = "complemento")
    private String complemement;
    
}
