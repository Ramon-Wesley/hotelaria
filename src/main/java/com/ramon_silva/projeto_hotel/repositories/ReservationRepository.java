package com.ramon_silva.projeto_hotel.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ramon_silva.projeto_hotel.models.ReservationModel;

public interface ReservationRepository extends JpaRepository<ReservationModel, Long> {
    
   
 }
