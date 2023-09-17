package com.ramon_silva.projeto_hotel.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.HotelImage;

public interface HotelImageRepository extends JpaRepository<HotelImage, Long> {
    boolean existsByImageUrl(String imageUrl);
    Page<HotelImage> findAllByHotelId(Long id, Pageable pageable);
    List<HotelImage> findAllByHotelId(Long id);
    Optional<HotelImage> findByHotelIdAndId(Long hotel_id,Long id);
}
