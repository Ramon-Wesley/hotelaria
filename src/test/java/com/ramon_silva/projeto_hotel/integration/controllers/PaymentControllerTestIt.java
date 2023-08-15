package com.ramon_silva.projeto_hotel.integration.controllers;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.AddressRepository;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
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
public class PaymentControllerTestIt {

    
    @Autowired
    private PaymentRepository paymentRepository;

   @Autowired
   private ReservationRepository reservationRepository;

   @Autowired
    private ClientRepository clientRepository;
   
    @Autowired
    private RoomRepository roomRepository;
   
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private MockMvc mockMvc;

    private PaymentModel paymentModel;

    private PaymentDto paymentDto;

  
    private ReservationModel reservationModel;
    private AddressModel addressModel;
    private HotelModel hotelModel;
    private ClientModel clientModel;
    private RoomModel roomModel;
    private Set<Reservation_serviceModel> services=new HashSet<>();

    private ObjectMapper objectMapper=new ObjectMapper();

@BeforeEach
@Transactional
void setUp(){
  addressModel=addressRepository.save(AddressCreator.newAddressModel());

  clientModel=ClientCreator.newClientModel();
  clientModel.setAddress(addressModel);
  clientModel=clientRepository.save(clientModel);

  addressModel=addressRepository.save(AddressCreator.newAddressModel2());
  
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
  reservationModel.setStatus(StatusEnum.CONFIRM);
  reservationModel.setReservation_service(services);
  reservationModel=reservationRepository.save(reservationModel);
  
}

@AfterEach
void setDown(){
  paymentRepository.deleteAll();
  reservationRepository.deleteAll();
  roomRepository.deleteAll();
  hotelRepository.deleteAll();
  clientRepository.deleteAll();
  addressRepository.deleteAll();
}
    @Test
    @DisplayName("Sucesso ao realizar um pagamento de reserva")
    void Test_payment_success() {

      
      try {
          paymentDto=new PaymentDto(null, null, PaymentMethodEnum.MONEY, null, null, 0);
        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=reservationModel.getId();
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
              .andExpect(MockMvcResultMatchers.status().isCreated());
      } catch (Exception e) {
          // Print the error message and stack trace
          e.printStackTrace();
      }
    
        
    }
    
  @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva nao confirmada")
    void Test_payment_error_with_reservation_pending(){
      
    }

     @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva inexistente")
    void Test_payment_error_with_reservation_notExists(){
      
    }

    @Test
    @DisplayName("Deletar com sucesso um pagamento")
    void Test_delete_payment_by_id(){
          

    }
    
      @Test
    @DisplayName("Deletar um pagamento inexistente")
    void Test_delete_payment_by_id_notExists(){
             
    }

    @Test
    @DisplayName("Atualizar pagamento existente")
    void Test_update_payment_by_id(){
           
    }

    @Test
    @DisplayName("Atualizar pagamento inexistente")
    void Test_update_payment_with_notExist_id(){
              
    }

  @Test
    @DisplayName("selecionar com sucesso um pagamento")
    void Test_get_payment_by_id(){
       
    }

      @Test
    @DisplayName("selecionar um pagamento inexistente")
    void Test_get_payment_notExist_by_id(){
   
       
    }


    @Test
    @DisplayName("Listar pagamentos")
    void Test_get_all_payment(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        String sortOrder = "asc";
        int numberOfElements= 2;
        int totalPages= 1;
        long totalElments=2;
      }

     @Test
    @DisplayName("Listar pagamentos com lista vazia")
    void Test_get_all_payment_empty_list(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        String sortOrder = "asc";
        int numberOfElements= 0;
        int totalPages= 1;
        long totalElments=0;
    

     
     }
}
