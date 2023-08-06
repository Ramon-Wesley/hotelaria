package com.ramon_silva.projeto_hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.ClientModel;

public interface ClientRepository extends JpaRepository<ClientModel,Long>{
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    boolean existsByEmailAndIdNot(String email,Long id);
    boolean existsByCpfAndIdNot(String cpf,Long id);
    ClientModel findByEmail(String email);
}
