package com.ramon_silva.projeto_hotel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.HotelImage;

public interface HotelImageRepository extends JpaRepository<HotelImage,Long> {
    
}
