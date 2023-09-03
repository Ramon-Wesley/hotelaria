package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.models.EmployeeModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
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
public class EmployeeDto{

     private Long id;
    
    @NotBlank
    @Length(min = 2)
     private String name;
    
    @NotBlank
    @Email
     private String email;
    
    @NotBlank
     private String phone;
    
     @NotNull
     private AddressDto address;
    
    
     private OfficeDto office;

    @Min(0)
     private double remunaration;
    
    @Future
    @NotBlank
     private LocalDate contractDate;
    
    @Future
     private LocalDate shutdownDate;
    
    @NotNull
     private boolean situation;
}
