package com.ramon_silva.projeto_hotel.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;

public interface Reservation_serviceRepository extends JpaRepository<Reservation_serviceModel,Long>{

   Page<Reservation_serviceModel> findAllByReservationId(Long reservationId, Pageable pageable);
   Optional<Reservation_serviceModel> findByReservationIdAndServicoId(Long reservationId, Long service_id);
   
}
