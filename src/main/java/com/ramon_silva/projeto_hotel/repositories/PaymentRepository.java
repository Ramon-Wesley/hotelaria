package com.ramon_silva.projeto_hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.PaymentModel;

public interface PaymentRepository extends JpaRepository<PaymentModel,Long>{
    
}
