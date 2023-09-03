package com.ramon_silva.projeto_hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.GuestModel;

public interface GuestRepository extends JpaRepository<GuestModel,Long>{
    boolean existsByEmailAndCpf(String email,String Cpf);
    boolean existsByEmailAndCpfAndIdNot(String email,String cpf,Long id);
    GuestModel findByEmail(String email);
}
