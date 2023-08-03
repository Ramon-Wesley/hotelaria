package com.ramon_silva.projeto_hotel.models;

import com.ramon_silva.projeto_hotel.dto.OfficeDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name="empregos")
@Table(name="empregos")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OfficesModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",nullable = false,unique = true)
    @NotNull
    private String name;

    public OfficesModel(OfficeDto office){
        this.id=office.id();
        this.name=office.name();
    }
}
