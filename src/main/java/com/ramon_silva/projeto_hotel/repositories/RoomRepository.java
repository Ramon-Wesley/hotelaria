package com.ramon_silva.projeto_hotel.repositories;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.RoomModel;

public interface RoomRepository extends JpaRepository<RoomModel,Long>{
    Page<RoomModel> findAllByHotelName(String name,Pageable pageable);
}
