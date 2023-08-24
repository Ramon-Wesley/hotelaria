package com.ramon_silva.projeto_hotel.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;

public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {
    @Query(value = "SELECT COUNT(*) > 0 FROM reservas  " +
    "WHERE quarto_id = :quarto_id " +
    "AND data_de_check_in <= :checkOut " +
    "AND data_de_check_out >= :checkIn",nativeQuery=true)
boolean hasConflictingReservations(
@Param("quarto_id") Long room,
@Param("checkIn") LocalDate checkIn,
@Param("checkOut") LocalDate checkOut);

@Query(value = "SELECT COUNT(*) > 0 FROM reservas  " +
    "WHERE id != :id " +
    "AND quarto_id = :quarto_id"+
    "AND data_de_check_in <= :checkOut " +
    "AND data_de_check_out >= :checkIn",nativeQuery=true)
boolean hasConflictingReservationsDatesWithIdNotEquals(
@Param("id") Long reservation,
@Param("quarto_id") Long room,
@Param("checkIn") LocalDate checkIn,
@Param("checkOut") LocalDate checkOut);
   
 }
