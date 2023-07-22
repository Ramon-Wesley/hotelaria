package com.ramon_silva.projeto_hotel.models;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "clientes")
@AllArgsConstructor
@NoArgsConstructor
public class ClientModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @NotBlank
    @Length(min = 2)
    @Column(name="nome")
    private String name;
    
    @NotBlank
    @Email
    @Column(unique=true)
    private String email;
    
    @NotBlank
    @Column(name="telefone")
    private String phone;


    @OneToOne(   
     optional = false,
     fetch = FetchType.LAZY,
     orphanRemoval = true)
    @JoinColumn(name = "endereco_id")
    private AddressModel address;
    
}
