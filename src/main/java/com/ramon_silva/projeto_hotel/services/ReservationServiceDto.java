package com.ramon_silva.projeto_hotel.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.util.Constants;

public class ReservationServiceDto implements ReservationService {

    private ReservationRepository reservationRepository;
    public void ReservationDto(ReservationRepository reservationRepository){
        this.reservationRepository=reservationRepository;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {

        int count=reservationRepository.countConflitReservation(reservationDto.room().id(), reservationDto.checkInDate(), reservationDto.checkOutDate());
        if(count == 0){
           ReservationModel reservationModel=reservationRepository.save(new ReservationModel(reservationDto));
           reservationModel.setTotal_pay(ReservationServiceDto.totalPrice(reservationModel.getCheckInDate(),reservationModel.getCheckOutDate(), reservationModel.getRoom().getPrice()));
           return new ReservationDto(reservationModel);
        }

            throw new GeralException(Constants.CONFLICT_DATE_RESERVATION);
        
        
    }

    @Override
    public ReservationDto cancelReservation(Long id) {
      ReservationModel reservationModel=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reservas","id",id));
      if(reservationModel.getStatus().equals(StatusEnum.PENDING)){
        reservationModel.setStatus(StatusEnum.CANCELED);
        reservationRepository.save(reservationModel);
        return new ReservationDto(reservationModel);
      }
      throw new GeralException(Constants.STATUS_RESERVATION_ERROR);
    }

    @Override
    public ReservationDto confirmReservation(Long id) {
         ReservationModel reservationModel=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reservas","id",id));
      if(reservationModel.getStatus().equals(StatusEnum.PENDING)){
        reservationModel.setStatus(StatusEnum.CONFIRM);
        reservationRepository.save(reservationModel);
        return new ReservationDto(reservationModel);
      }
      throw new GeralException(Constants.STATUS_RESERVATION_ERROR);
    }

    @Override
    public ReservationDto updateReservation(Long id, ReservationDto reservationDto) {
        ReservationModel reservationModel=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reservas","id",id));
      if(reservationModel.getStatus().equals(StatusEnum.PENDING)){
        reservationModel.setTotal_pay(ReservationServiceDto.totalPrice(reservationModel.getCheckInDate(),reservationModel.getCheckOutDate(), reservationModel.getRoom().getPrice()));
        reservationRepository.save(reservationModel);
        return new ReservationDto(reservationModel);
      }
       throw new GeralException(Constants.CONFLICT_DATE_RESERVATION);
    }

    @Override
    public ReservationDto getReservationById(Long id) {
      
    ReservationModel reservation=reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Reserva", "id", id));
    return new ReservationDto(reservation);
    }

    @Override
    public PageDto<ReservationDto> getAllReservation(int pageNumber, int pageSize, String sortBy, String sortOrder) {
       Sort sort=sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
       Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
       Page<ReservationModel> page=reservationRepository.findAll(pageable);
       List<ReservationDto> reservationDtos=page.getContent().stream().map(ReservationDto::new).collect(Collectors.toList());
       PageDto<ReservationDto> result=new PageDto<>(reservationDtos,page.getNumber(), page.getNumberOfElements(), page.getSize(),
       page.getTotalPages(), page.getTotalElements());
       return result;
    }

    public static double totalPrice(LocalDate date1, LocalDate date2,double price){
           long days= ChronoUnit.DAYS.between(date1, date2);
           double priceReservation=price*days;
           return priceReservation;
    }

    
}
