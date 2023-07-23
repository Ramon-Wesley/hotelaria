package com.ramon_silva.projeto_hotel.models;


import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

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
import lombok.NoArgsConstructor;

@Entity
@Table(name = "funcionarios")
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    @NotBlank
    @Length(min = 2)
    @Column(name="nome")
    private String name;
    
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;
    
    @NotBlank
    @Column(name="telefone")
    private String phone;
    
 
    @OneToOne(optional = false,
     fetch = FetchType.LAZY,
     orphanRemoval = true)
     @JoinColumn(name = "endereco_id")
    private AddressModel address;
    
    @Enumerated
    @Column(name="cargo")
    private OfficeEnum office;
    
    @Column(name="remuneracao")
    @Min(0)
    private double remunaration;
    
    @Future
    @NotBlank
    @Column(name="data_da_contratacao")
    private LocalDate contractDate;
    
    @Future
    @Column(name="data_do_desligamento")
    private LocalDate shutdownDate;
    
    @NotNull
    @Column(name="situacao")
    private boolean situation;
    
}
