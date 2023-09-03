package com.ramon_silva.projeto_hotel.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonProperty;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GuestDto implements Serializable{
    
    @JsonProperty("id")
private Long id;

@JsonProperty("name")
 @NotBlank
    @Length(min = 2)
private String name;

@JsonProperty("cpf")
  @CPF
    @NotNull
private String cpf;

@JsonProperty("email")
  @NotBlank
    @Email
private String email;

@JsonProperty("phone")
@NotBlank
private String phone;

@JsonProperty("address")
@NotNull
private AddressDto address;
  

    
}
