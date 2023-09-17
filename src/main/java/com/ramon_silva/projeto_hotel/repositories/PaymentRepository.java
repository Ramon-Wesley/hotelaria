package com.ramon_silva.projeto_hotel.repositories;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;



public interface PaymentRepository extends JpaRepository<PaymentModel,Long>{

    Page<PaymentModel> findAllByReservationGuestNameContainingIgnoreCase(String guestModel,Pageable pageable);
    boolean existsByReservationId(Long id);
    boolean existsByReservationIdAndIdNot(Long id,Long id_not);
   Optional<PaymentModel> findByReservationId(Long id);


    

}
