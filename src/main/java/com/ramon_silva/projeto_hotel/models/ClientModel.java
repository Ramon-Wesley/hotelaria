package com.ramon_silva.projeto_hotel.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientModel {


    public ClientModel(Long id,ClientDto client){
        this.id=id;
        this.name=client.name();
        this.cpf=client.cpf();
        this.email=client.email();
        this.phone=client.phone();
        this.address=new AddressModel(client.address().id(),client.address());
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @NotNull
    @Length(min = 2)
    @Column(name="nome")
    private String name;
    
    @CPF
    @Column(name="cpf",unique = true)
    @NotBlank
    @NotNull
    private String cpf;

    @NotBlank
    @NotNull 
    @Email
    @Column(name="email", unique=true)
    private String email;
    
    @NotBlank
    @NotNull
    @Column(name="telefone")
    private String phone;


    @NotNull
    @OneToOne(   
     optional = false,
     fetch = FetchType.LAZY,
     orphanRemoval = true,
     cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private AddressModel address;
    
}
