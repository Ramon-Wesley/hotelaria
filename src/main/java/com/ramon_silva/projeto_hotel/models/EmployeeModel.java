package com.ramon_silva.projeto_hotel.models;


import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.dto.EmployeeDto;
import com.ramon_silva.projeto_hotel.enums.OfficeEnum;

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
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "funcionarios")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class EmployeeModel {
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
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @NotNull
    @Column(name="telefone")
    private String phone;
    
 
    @NotNull
    @OneToOne(optional = false,
     fetch = FetchType.LAZY,
     orphanRemoval = true)
     @JoinColumn(name = "endereco_id")
    private AddressModel address;
    
    @NotNull
    @Enumerated
    @Column(name="cargo")
    private OfficeEnum office;
    
    @NotNull
    @Column(name="remuneracao")
    @Min(0)
    private double remunaration;
    
    @NotNull
    @Future
    @NotBlank
    @Column(name="data_da_contratacao")
    private LocalDate contractDate;
    
    
    @Future
    @Column(name="data_do_desligamento")
    private LocalDate shutdownDate;
    
    @NotNull
    @Column(name="situacao")
    private Boolean situation;

    public EmployeeModel(EmployeeDto employeeDto){
        this.id=employeeDto.id();
        this.name=employeeDto.name();
        this.phone=employeeDto.phone();
        this.office=employeeDto.office();
        this.remunaration=employeeDto.remunaration();
        this.email=employeeDto.email();
        this.address=new AddressModel(employeeDto.address());
        this.contractDate=employeeDto.contractDate();
        this.shutdownDate=employeeDto.shutdownDate();
        this.situation=employeeDto.situation();
    }
    
}
