package com.ramon_silva.projeto_hotel.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;

import com.ramon_silva.projeto_hotel.dto.HotelDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hoteis")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HotelModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Length(min = 2)
    @Column(name = "nome")
    private String name;

    @CNPJ
    @Column(name = "cnpj", unique = true)
    private String cnpj;

    @NotBlank
    @NotNull
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank
    @NotNull
    @Column(name = "telefone")
    private String phone;

    @Column(name = "descricao")
    private String description;

    @NotNull
    @Column(name = "classificacao")
    private String classification;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomModel> rooms = new HashSet<>();

    @OneToOne(optional = false, fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private AddressModel address;

    @OneToMany(mappedBy = "hotelModel", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<HotelImage> hotelImages = new ArrayList<>();
    
    @Column(name="ativo")
    private Boolean active=true;
}