package com.ramon_silva.projeto_hotel.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.RolModel;
import com.ramon_silva.projeto_hotel.models.UsersModel;



public interface RolRepository extends JpaRepository<UsersModel,Long>{
    
    public Optional<RolModel> findByLogin(String login);
}
