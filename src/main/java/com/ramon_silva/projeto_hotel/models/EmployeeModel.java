package com.ramon_silva.projeto_hotel.models;


import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;




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
     orphanRemoval = true,
     cascade = CascadeType.ALL)
     @JoinColumn(name = "endereco_id")
    private AddressModel address;
    
    @NotNull
    @OneToOne(optional = false)
    @JoinColumn(name="cargo")
    private OfficesModel office;
    
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


    
}
