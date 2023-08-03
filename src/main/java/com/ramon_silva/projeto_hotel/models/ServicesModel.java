package com.ramon_silva.projeto_hotel.models;



import com.ramon_silva.projeto_hotel.dto.ServicesDto;

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

@Entity(name="servicos")
@Table(name = "servicos")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServicesModel {
    
    public ServicesModel(Long id,ServicesDto service){
        this.id=id;
        this.name=service.name();
        this.price=service.price();
        this.type_service=service.type_service();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nome",unique = true,nullable = false)
    @NotBlank
    @NotNull
    private String name;

    @NotNull
    @Column(name = "tipo_servico")
    private String type_service;

    @Column(name="preco")
    @NotNull
    private Double price;


}
