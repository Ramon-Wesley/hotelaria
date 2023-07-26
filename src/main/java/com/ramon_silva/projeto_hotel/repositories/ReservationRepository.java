package com.ramon_silva.projeto_hotel.repositories;

import java.time.LocalDate;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ramon_silva.projeto_hotel.models.ReservationModel;

public interface ReservationRepository extends JpaRepository<ReservationModel,Long>{
    
    @Query("select count(*) from Reservas where quarto_id =:quarto_id "+
    "and data_de_checkIn <= :data_checkout"+
    "and data_de_checkOut= :data_de_checkIn")
    int countConflitReservation(
        @Param("quarto_id") Long room_id,
        @Param("data_de_checkIn") LocalDate checkIn,
        @Param("data_checkout") LocalDate checkOut
    );

}
