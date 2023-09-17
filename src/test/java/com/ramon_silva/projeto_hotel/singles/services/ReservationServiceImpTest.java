package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.Reservation_serviceRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.repositories.ServicesRepository;
import com.ramon_silva.projeto_hotel.services.EmailServiceIMP;
import com.ramon_silva.projeto_hotel.services.ReservationServiceIMP;
import com.ramon_silva.projeto_hotel.util.ReservationCreator;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceImpTest {
    
    @InjectMocks
    private ReservationServiceIMP reservationServiceIMP;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private  GuestRepository guestRepository;
    
    @Mock
    private  RoomRepository roomRepository;
    
    @Mock
    private  ServicesRepository servicesRepository;
    
    @Mock
    private  Reservation_serviceRepository reservation_serviceRepository;
    
    @Mock
    private  EmailServiceIMP emailServiceIMP;

    @Mock
    private ModelMapper modelMapper;

    @Autowired
    private ModelMapper modelMapper2=new ModelMapper();
    private ReservationModel reservationModel;

    private ReservationDto reservationDto;

    private GuestModel guestModel;

    private RoomModel roomModel;



    @Test
    @DisplayName("Realizar a reserva com sucesso!")
    void Test_create_reservation_success(){
    reservationModel=ReservationCreator.newReservationModel();
    roomModel=reservationModel.getRoom();
    roomModel.setId(1L);
    guestModel=reservationModel.getGuest();
    guestModel.setId(1L);
    
    ReservationDto reservationDtoMapper= modelMapper2.map(reservationModel,ReservationDto.class);
    System.out.println(reservationDtoMapper.getGuest().getId());
    
    reservationModel.setId(1L);
    when(guestRepository.findById(guestModel.getId())).thenReturn(Optional.of(guestModel));
    when(roomRepository.findById(roomModel.getId())).thenReturn(Optional.of(roomModel));
    when(reservationRepository.hasConflictingReservations(anyLong(), any(LocalDate.class), any(LocalDate.class))).thenReturn(false);
    when(reservationRepository.save(any(ReservationModel.class))).thenReturn(reservationModel);
    when(modelMapper.map(eq(reservationDtoMapper), eq(ReservationModel.class))).thenReturn(reservationModel);
    reservationDtoMapper.setId(1L); 
    when(modelMapper.map(eq(reservationModel), eq(ReservationDto.class))).thenReturn(reservationDtoMapper); 
   
    reservationDto=reservationServiceIMP.create(reservationDtoMapper);

    verify(guestRepository,times(1)).findById(guestModel.getId());
    verify(roomRepository,times(1)).findById(roomModel.getId());
    verify(reservationRepository,times(1)).save(any(ReservationModel.class));

    assertNotNull(reservationDto.getId());
    assertEquals(guestModel.getId(),reservationDto.getGuest().getId());
    assertEquals(roomModel.getId(),reservationDto.getRoom().getId());
    }
}
