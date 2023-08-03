package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.lang.Nullable;

import com.ramon_silva.projeto_hotel.models.ClientModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientDto(
    
    @Nullable
    Long id,
    
    @NotBlank
    @Length(min = 2)
    String name,

    @CPF
    @NotNull
    String cpf,

    @NotBlank
    @NotNull
    @Email
    String email,
    
    @NotBlank
    @NotNull
    String phone,

    @NotNull
    AddressDto address

) {
     public ClientDto(ClientModel client){
            this(client.getId(), client.getName(), client.getCpf(), client.getEmail(), client.getPhone(), new AddressDto(client.getAddress()));
    }
}








