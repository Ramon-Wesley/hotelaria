package com.ramon_silva.projeto_hotel.dto;


import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;


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
public class GuestDto  {

  private Long id;

  @NotBlank
  @Length(min = 2)
  private String name;

  @CPF
  @NotNull
  private String cpf;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String phone;

  @NotNull
  private AddressDto address;

  private Boolean active = true;
}
