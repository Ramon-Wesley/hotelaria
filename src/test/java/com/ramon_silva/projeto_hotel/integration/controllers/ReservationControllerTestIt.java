package com.ramon_silva.projeto_hotel.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ramon_silva.projeto_hotel.dto.ReservationDatesDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.services.EmailServiceIMP;
import com.ramon_silva.projeto_hotel.util.AddressCreator;
import com.ramon_silva.projeto_hotel.util.ClientCreator;
import com.ramon_silva.projeto_hotel.util.HotelCreator;
import com.ramon_silva.projeto_hotel.util.ReservationCreator;
import com.ramon_silva.projeto_hotel.util.RoomCreator;

import jakarta.transaction.Transactional;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerTestIt {
 
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EmailServiceIMP emailService;
    
    @Autowired
    private ClientRepository clientRepository;
   
    @Autowired
    private RoomRepository roomRepository;
   
    @Autowired
    private HotelRepository hotelRepository;

    @MockBean
    private EmailServiceIMP mockEmailService; 

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ModelMapper modelMapper;
    
    private ReservationModel reservationModel;
    private ReservationDto reservationDto;
    private ClientModel clientModel;
    private AddressModel addressModel;
    private HotelModel hotelModel;
    private RoomModel roomModel;
    private Set<Reservation_serviceModel> services=new HashSet<>();
    private  ReservationModel reservationModelResult;

private ObjectMapper objectMapper=new ObjectMapper();
@BeforeEach
@Transactional
void setUp()
{
  
    objectMapper.registerModule(new JavaTimeModule()); 
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    SaveReservation(reservationRepository, clientRepository, roomRepository, hotelRepository);
    SaveDates(clientRepository, roomRepository, hotelRepository);
    reservationModelResult=new ReservationModel(null, clientModel, 
    roomModel,
    LocalDate.now().plusYears(1), LocalDate.now().plusYears(1).plusDays(2), null, null, null);
    
}
    @Test
    @DisplayName("Erro ao salvar uma reserva com datas conflitantes em um mesmo quarto")
    void Test_create_error_reservation_room_with_dates_conflict() throws Exception{
    reservationDto=modelMapper.map(reservationModel,ReservationDto.class); 
    reservationDto.setId(null); 
      String json= objectMapper.writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        ).andDo(MockMvcResultHandlers.log())
         .andExpect(MockMvcResultMatchers.status().isConflict());
        
      verify(emailService,times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());
    
    }

    @Test
    @DisplayName("Salvar uma reserva com sucesso")
    void Test_create_reservation_success() throws Exception{
     reservationDto=modelMapper.map(reservationModelResult,ReservationDto.class);
      String json= objectMapper.writeValueAsString(reservationDto); 
        mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        ).andDo(MockMvcResultHandlers.log())
         .andExpect(MockMvcResultMatchers.status().isCreated())
         .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

      verify(emailService,times(1)).sendEmail(any(EmailModel.class), any(Object.class), anyString());
    
    }

    @Test
    @DisplayName("Erro ao salvar uma reserva com as datas invalidas!")
    void Test_create_reservation_error_with_dates_empty() throws Exception{
       reservationDto=modelMapper.map(reservationModelResult,ReservationDto.class);
       reservationDto.setCheckInDate(LocalDate.now().minusWeeks(2));
       reservationDto.setCheckOutDate(LocalDate.now().minusWeeks(1)); 
      
        String json= objectMapper.writeValueAsString(reservationDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        ).andDo(MockMvcResultHandlers.log())
         .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

@Test
@DisplayName("Criar uma reserva com um cliente inexistente")
void Test_create_reservation_with_client_id_not_exists() throws Exception{
  reservationDto=modelMapper.map(reservationModelResult,ReservationDto.class);
  reservationDto.getClient().setId(99L);    
  String json= objectMapper.writeValueAsString(reservationDto);  
        mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        ).andDo(MockMvcResultHandlers.log())
         .andExpect(MockMvcResultMatchers.status().isNotFound());
}
@Test
@DisplayName("Criar uma reserva com um quarto inexistente")
void Test_create_reservation_with_room_id_not_exists() throws Exception{
      reservationDto=modelMapper.map(reservationModelResult,ReservationDto.class);
     reservationDto.getRoom().setId(99L);
    
  String json= objectMapper.writeValueAsString(reservationDto);  
        mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json)
        ).andDo(MockMvcResultHandlers.log())
         .andExpect(MockMvcResultMatchers.status().isNotFound());

      }
@Test
@DisplayName("Encontrar uma reserva pelo id")
void Test_success_get_by_id_reservation() throws Exception{

        long id_reservation=reservationModel.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/reserva/{id_reserva}",id_reservation)
        .contentType(MediaType.APPLICATION_JSON)
        )  .andExpect(MockMvcResultMatchers.status().isOk());
}


     public void SaveReservation(
    
      ReservationRepository reservationRepository,
  
       ClientRepository clientRepository,
     
       RoomRepository roomRepository,
     
       HotelRepository hotelRepository
     ){
       
  addressModel=AddressCreator.newAddressModel();
    
  clientModel=new ClientModel();
  clientModel=ClientCreator.newClientModel();
  clientModel.setAddress(addressModel);

  
  addressModel=AddressCreator.newAddressModel2();
  
  hotelModel=HotelCreator.newModelHotel();
  hotelModel.setAddress(addressModel);

  
  clientModel=clientRepository.save(clientModel);
  hotelModel=hotelRepository.save(hotelModel);

  roomModel=RoomCreator.newModelRoom();
  roomModel.setHotel(hotelModel);

  roomModel=roomRepository.save(roomModel);

  reservationModel=ReservationCreator.newReservationModel();
  reservationModel.setClient(clientModel);
  reservationModel.setRoom(roomModel);
  reservationModel.setStatus(StatusEnum.PENDING);
  reservationModel=reservationRepository.save(reservationModel);


     }

      public void SaveDates(
    
       ClientRepository clientRepository,
     
       RoomRepository roomRepository,
     
       HotelRepository hotelRepository
     ){
       
  addressModel=AddressCreator.newAddressModel();
    
  clientModel=new ClientModel();
  clientModel=ClientCreator.newClientModel2();
  clientModel.setAddress(addressModel);

  
  addressModel=AddressCreator.newAddressModel3();
  
  hotelModel=HotelCreator.newModelHotel2();
  hotelModel.setAddress(addressModel);

  
  clientModel=clientRepository.save(clientModel);
  hotelModel=hotelRepository.save(hotelModel);

  roomModel=RoomCreator.newModelRoom2();
  roomModel.setHotel(hotelModel);

  roomModel=roomRepository.save(roomModel);




     }

}
