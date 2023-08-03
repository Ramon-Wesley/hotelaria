package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.models.EmployeeModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EmployeeDto(

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
     AddressDto address,
    
    
     OfficeDto office,

    @Min(0)
     double remunaration,
    
    @Future
    @NotBlank
     LocalDate contractDate,
    
    @Future
     LocalDate shutdownDate,
    
    @NotNull
     boolean situation

) {
 
     public EmployeeDto(EmployeeModel employeeModel){
          this(employeeModel.getId(), employeeModel.getName(), employeeModel.getEmail(), 
          employeeModel.getPhone(), new AddressDto(employeeModel.getAddress()), 
          new OfficeDto(employeeModel.getOffice()), employeeModel.getRemunaration(), employeeModel.getContractDate(),
          employeeModel.getShutdownDate(),employeeModel.getSituation());
     }
}
