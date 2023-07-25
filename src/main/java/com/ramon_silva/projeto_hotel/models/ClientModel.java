package com.ramon_silva.projeto_hotel.models;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.dto.ClientDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientModel {


    public ClientModel(ClientDto client){
        this.id=client.id();
        this.name=client.name();
        this.email=client.email();
        this.phone=client.phone();
        this.address=new AddressModel(client.address());

      
    };
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @NotBlank
    @NotNull
    @Length(min = 2)
    @Column(name="nome")
    private String name;
    
    @NotBlank
    @NotNull 
    @Email
    @Column(unique=true)
    private String email;
    
    @NotBlank
    @NotNull
    @Column(name="telefone")
    private String phone;


    @NotNull
    @OneToOne(   
     optional = false,
     fetch = FetchType.EAGER,
     orphanRemoval = true,
     cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private AddressModel address;
    
}
