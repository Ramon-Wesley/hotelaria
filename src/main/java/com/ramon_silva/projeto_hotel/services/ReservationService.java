package com.ramon_silva.projeto_hotel.services;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;

public interface ReservationService {
    public ReservationDto createReservation(ReservationDto reservationDto,Long client_id,Long room_id);
    public ReservationDto cancelReservation(Long id);
    public ReservationDto confirmReservation(Long id);
    public ReservationDto updateReservation(Long id,ReservationDto reservationDto);
    public ReservationDto getReservationById(Long id);
    public PageDto<ReservationDto> getAllReservation(int pageNumber,int pageSize,String sortBy,String sortOrder);
}
