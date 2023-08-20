package com.ramon_silva.projeto_hotel.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;

import jakarta.transaction.Transactional;


public interface PaymentRepository extends JpaRepository<PaymentModel,Long>{

    Page<PaymentModel> findAllByReservationClientNameContainingIgnoreCase(String clientModel,Pageable pageable);
    boolean existsByReservationId(Long id);
    boolean existsByReservationIdAndIdNot(Long id,Long id_not);

   /*
    * 
    @Transactional
    @Modifying
    @Query("DELETE FROM pagamentos WHERE id <> :id")
    void deleteAllAndIdNot(@Param("id") Long id);
    */
    

}
