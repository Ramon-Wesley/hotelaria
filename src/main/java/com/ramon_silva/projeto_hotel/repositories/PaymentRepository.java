package com.ramon_silva.projeto_hotel.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.PaymentModel;



public interface PaymentRepository extends JpaRepository<PaymentModel,Long>{

    Page<PaymentModel> findAllByReservationClientNameContainingIgnoreCase(String clientModel,Pageable pageable);
    boolean existsByReservationId(Long id);
    boolean existsByReservationIdAndIdNot(Long id,Long id_not);


    

}
