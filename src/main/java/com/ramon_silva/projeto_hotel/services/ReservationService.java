package com.ramon_silva.projeto_hotel.services;


import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;


public interface ReservationService {
    public ReservationDto createReservation(ReservationDto reservationDto,Long client_id,Long room_id);
    public ReservationDto cancelReservation(Long id);
    public ReservationDto confirmReservation(Long id);
    public ReservationDto updateReservation(Long id,ReservationDto reservationDto);
    public ReservationDto getReservationById(Long id);
    public PageDto<ReservationDto> getAllReservation(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public void addServices(Long reservation_id,Long service_id);
    public void removeServices(Long reservation_id,Long service);
    public PageDto<Reservation_serviceDto> getAllServicesReservation(Long reservation_id,int pageNumber,int pageSize,String sortBy,String sortOrder);
    public PageDto<Reservation_serviceDto> getAll(int pageNumber,int pageSize,String sortBy,String sortOrder);
   
}
