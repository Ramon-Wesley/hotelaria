package com.ramon_silva.projeto_hotel.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ramon_silva.projeto_hotel.models.ReservationModel;

public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM reservas r WHERE r.data_de_check_in = :checkIn AND r.data_de_check_out = :checkOut")
    boolean existsReservationWithDates(@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);


   
 }
