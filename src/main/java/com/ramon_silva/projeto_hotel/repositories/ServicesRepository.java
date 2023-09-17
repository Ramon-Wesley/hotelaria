package com.ramon_silva.projeto_hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.ServicesModel;
import java.util.List;


public interface ServicesRepository extends JpaRepository<ServicesModel,Long>{
    List<ServicesModel> findAllByIdInAndActive(List<Long> ids, boolean active);

}
