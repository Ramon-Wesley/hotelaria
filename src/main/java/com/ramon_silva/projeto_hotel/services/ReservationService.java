package com.ramon_silva.projeto_hotel.services;


import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDatesDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;



public interface ReservationService {
    public ReservationDto createReservation(ReservationDatesDto reservationDto,Long client_id,Long room_id);
    public ReservationDto updateReservation(Long id,Long room_id,Long client_id, ReservationDatesDto reservationDto);
    public ReservationDto getReservationById(Long id);
    public PageDto<ReservationDto> getAllReservation(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public PageDto<Reservation_serviceDto> getAllServicesReservation(Long reservation_id,int pageNumber,int pageSize,String sortBy,String sortOrder);
    public PageDto<Reservation_serviceDto> getAllServicesReservations(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public Reservation_serviceDto getServiceReservationById(Long id_reservation, Long service_id);
    public void addServices(Long reservation_id,Long service_id);
    public void removeServices(Long reservation_id,Long service);
   
}
