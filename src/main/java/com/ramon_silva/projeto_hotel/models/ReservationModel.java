package com.ramon_silva.projeto_hotel.models;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "reservas")
@Table(name = "reservas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservationModel {
 

    public ReservationModel(Long id,ReservationDto reservationDto){
        this.id=id;
        this.checkInDate=reservationDto.checkInDate();
        this.checkOutDate=reservationDto.checkOutDate();
        this.total_pay=reservationDto.total_pay();
        this.status=reservationDto.status();
        this.client=new ClientModel(reservationDto.client().id(),reservationDto.client());
        this.room=new RoomModel(id, reservationDto.room());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="cliente_id")
    private ClientModel client;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="quarto_id")
    private RoomModel room;

    
    @Future
    @NotNull
    @Column(name="data_de_check_in",nullable = false)
    private LocalDate checkInDate;

    @Future
    @NotNull
    @Column(name="data_de_check_out",nullable = false)
    private LocalDate checkOutDate;
    
    @Enumerated
    @NotNull
    private StatusEnum status;


    @Column(name = "total_a_pagar")
    @NotNull
    private Double total_pay;
    

    @OneToMany(mappedBy = "reservation")
    private Set<Reservation_serviceModel> reservation_service=new HashSet<>();

}
