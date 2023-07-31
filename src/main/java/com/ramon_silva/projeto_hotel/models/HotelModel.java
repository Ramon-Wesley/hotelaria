package com.ramon_silva.projeto_hotel.models;


import java.util.HashSet;
import java.util.Set;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.dto.HotelDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hoteis")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelModel {


    public HotelModel(Long id,HotelDto hotel){
        this.id=hotel.id();
        this.name=hotel.name();
        this.address=new AddressModel(null,hotel.address());
        this.classification=hotel.classification();
        this.description=hotel.description();
    };
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 2)
    @Column(name = "nome")
    private String name;

    @Column(name = "descricao")
    private String description;

    @Min(1)
    @Max(5)
    @NotNull
    @Column(name = "classificacao")
    private int classification;


    @OneToMany(mappedBy ="hotel",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<RoomModel> rooms= new HashSet<>();

    @OneToOne(   
     optional = false,
     fetch = FetchType.LAZY,
     orphanRemoval = true,
     cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private AddressModel address;
}