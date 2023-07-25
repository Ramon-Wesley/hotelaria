package com.ramon_silva.projeto_hotel.dto;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.models.ClientModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClientDto(
    
    Long id,
    
    @NotBlank
    @Length(min = 2)
    String name,
    
    @NotBlank
    @Email
    String email,
    
    @NotBlank
    String phone,

    @NotNull
    AddressDto address

) {
     public ClientDto(ClientModel client){
        
        this(client.getId(), client.getName(), client.getEmail(), client.getPhone(), new AddressDto(client.getAddress()));
      
    };
    
}
