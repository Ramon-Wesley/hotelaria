package com.ramon_silva.projeto_hotel.models;
import org.hibernate.validator.constraints.Length;

import com.ramon_silva.projeto_hotel.enums.UsersEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@AllArgsConstructor
@NoArgsConstructor
public class UsersModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 2)
    @Column(name="login",unique = true)
    private String login;

    @NotBlank
    @Length(min = 8)
    @Column(name="senha")
    private String password;

    @Enumerated
    @Column(name="tipo_usuario")
    private UsersEnum typeUser;
}
