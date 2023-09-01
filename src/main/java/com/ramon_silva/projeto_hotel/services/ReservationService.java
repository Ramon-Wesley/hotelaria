package com.ramon_silva.projeto_hotel.services;


import java.util.List;
import java.util.Set;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDatesDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import com.ramon_silva.projeto_hotel.models.ServicesModel;



public interface ReservationService {
    public ReservationDto createReservation(ReservationDto reservationDto);
    public ReservationDto updateReservation(Long id,ReservationDto reservationDto);
    public ReservationDto getReservationById(Long id);
    public PageDto<ReservationDto> getAllReservation(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public PageDto<Reservation_serviceDto> getAllServicesReservation(Long reservation_id,int pageNumber,int pageSize,String sortBy,String sortOrder);
    public PageDto<Reservation_serviceDto> getAllServicesReservations(int pageNumber,int pageSize,String sortBy,String sortOrder);
    public Reservation_serviceDto getServiceReservationById(Long id_reservation, Long service_id);
    public void addServices(Long reservation_id,List<ServicesDto> reservation_serviceDtos);
    public void removeServices(Long reservation_id,Long service);
   
}
