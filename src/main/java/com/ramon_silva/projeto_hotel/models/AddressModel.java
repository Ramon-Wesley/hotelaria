package com.ramon_silva.projeto_hotel.models;


import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.dto.AddressDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enderecos")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressModel {


    public AddressModel(AddressDto address){
        this.id=address.id();
        this.country=address.country();
        this.state=address.state();
        this.zapCode=address.zapCode();
        this.city=address.city();
        this.neighborhood=address.neighborhood();
        this.number=address.number();
        this.complemement=address.complemement();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Column(name = "pais")
    @Length(min=2)
    private String country;

    @NotBlank
    @NotNull
    @Column(name = "estado")
    @Length(min=2)
    private String state;

    @NotBlank
    @NotNull
    @Column(name = "CEP")
    @Length(min = 10)
    private String zapCode;

    @NotNull        
    @NotBlank
    @Column(name = "cidade")
    @Length(min = 2)
    private String city;

    @NotBlank
    @NotNull
    @Column(name = "bairro")
    private String neighborhood;
    
    @NotBlank
    @NotNull
    @Column(name = "numero")
    private String number;

    @Column(name = "complemento")
    private String complemement;
    
}
