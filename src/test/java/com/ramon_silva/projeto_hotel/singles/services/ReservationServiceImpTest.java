package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
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
    private  ClientRepository clientRepository;
    
    @Mock
    private  RoomRepository roomRepository;
    
    @Mock
    private  ServicesRepository servicesRepository;
    
    @Mock
    private  Reservation_serviceRepository reservation_serviceRepository;
    
    @Mock
    private  EmailServiceIMP emailServiceIMP;
     
    private ModelMapper modelMapper;

    private ReservationModel reservationModel;

    private ReservationDto reservationDto;

    private ClientModel clientModel;

    private RoomModel roomModel;

@BeforeEach
void setup(){
    modelMapper=new ModelMapper();
}

    @Test
    @DisplayName("Realizar a reserva com sucesso!")
    void Test_create_reservation_success(){
    reservationModel=ReservationCreator.newReservationModel();
    roomModel=reservationModel.getRoom();
    clientModel=reservationModel.getClient();

    reservationModel.setId(1L);

    ReservationDto reservationDtoMapper= modelMapper.map(reservationModel,ReservationDto.class);
    
    when(clientRepository.findById(clientModel.getId())).thenReturn(Optional.of(clientModel));
    when(roomRepository.findById(roomModel.getId())).thenReturn(Optional.of(roomModel));
    when(reservationRepository.save(any(ReservationModel.class))).thenReturn(reservationModel);
   
    reservationDto=reservationServiceIMP.createReservation(reservationDtoMapper);

    verify(clientRepository,times(1)).findById(clientModel.getId());
    verify(roomRepository,times(1)).findById(roomModel.getId());
    verify(reservationRepository,times(1)).save(any(ReservationModel.class));

    assertNotNull(reservationDto.getId());
    assertEquals(clientModel.getId(),reservationDto.getClient().getId());
    assertEquals(roomModel.getId(),reservationDto.getRoom().getId());
    }
}
