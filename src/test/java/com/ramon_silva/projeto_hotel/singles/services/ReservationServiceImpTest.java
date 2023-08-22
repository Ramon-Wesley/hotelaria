package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private ReservationModel reservationModel;

    private ReservationDto reservationDto;

    private ClientModel clientModel;

    private RoomModel roomModel;
    @Test
    @DisplayName("Realizar a reserva com sucesso!")
    void Test_create_reservation_success(){
    reservationModel=ReservationCreator.newReservationModel();
    reservationDto=new ReservationDto(reservationModel);

    clientModel=reservationModel.getClient();
    roomModel=reservationModel.getRoom();

    reservationModel.setId(1L);

    when(clientRepository.findById(clientModel.getId())).thenReturn(Optional.of(clientModel));
    when(roomRepository.findById(roomModel.getId())).thenReturn(Optional.of(roomModel));
    when(reservationRepository.save(any(ReservationModel.class))).thenReturn(reservationModel);
    
    reservationDto=reservationServiceIMP.createReservation(reservationDto, clientModel.getId(), roomModel.getId());

    verify(clientRepository,times(1)).findById(clientModel.getId());
    verify(roomRepository,times(1)).findById(roomModel.getId());
    verify(reservationRepository,times(1)).save(any(ReservationModel.class));

    assertNotNull(reservationDto.id());
    assertEquals(clientModel.getId(),reservationDto.client().id());
    assertEquals(roomModel.getId(),reservationDto.room().id());
    }
}
