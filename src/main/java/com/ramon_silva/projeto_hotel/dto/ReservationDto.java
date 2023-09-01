package com.ramon_silva.projeto_hotel.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;

import jakarta.validation.constraints.Future;
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
public class ReservationDto{

      private Long id;

    @NotNull
    private ClientModel client;

    @NotNull
    private RoomModel room;

    
    @Future
    @NotNull
    private LocalDate checkInDate;

    @Future
    @NotNull
    private LocalDate checkOutDate;
    

    @NotNull
    private StatusEnum status;



    private Double total_pay;
    


    private Set<Reservation_serviceModel> reservation_service=new HashSet<>();

}
