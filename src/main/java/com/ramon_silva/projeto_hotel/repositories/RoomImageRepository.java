package com.ramon_silva.projeto_hotel.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ramon_silva.projeto_hotel.models.RoomImage;

public interface RoomImageRepository extends JpaRepository<RoomImage,Long>{
    boolean existsByImageUrl(String imageUrl);
    Page<RoomImage> findAllByRoomId(Long id, Pageable pageable);
    List<RoomImage> findAllByRoomId(Long id);
    Optional<RoomImage> findByRoomIdAndId(Long hotel_id,Long id);
    
}
