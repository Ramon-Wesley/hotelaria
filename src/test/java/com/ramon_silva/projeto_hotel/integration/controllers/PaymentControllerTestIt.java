package com.ramon_silva.projeto_hotel.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.services.EmailService;
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
public class PaymentControllerTestIt {

    
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailServiceIMP emailService;
    @MockBean
    private EmailServiceIMP mockEmailService; 

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    private MockMvc mockMvc;

   
    private static boolean dataInitialize=false;
    private PaymentModel paymentModel;

    private PaymentDto paymentDto;

  
    private ReservationModel reservationModel;
    private ClientModel clientModel;
    private AddressModel addressModel;
    private HotelModel hotelModel;
    private RoomModel roomModel;
    private Set<Reservation_serviceModel> services=new HashSet<>();

    private ObjectMapper objectMapper=new ObjectMapper();

@BeforeAll
@Transactional
void setUp(
   @Autowired
    ReservationRepository reservationRepository,

   @Autowired
     ClientRepository clientRepository,
   
    @Autowired
     RoomRepository roomRepository,
   
    @Autowired
     HotelRepository hotelRepository
){
  if(!dataInitialize){

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
  reservationModel.setStatus(StatusEnum.CONFIRM);
  reservationModel.setReservation_service(services);
  reservationModel=reservationRepository.save(reservationModel);
  dataInitialize=true;
}

}

@AfterEach
void setDown(){
  paymentRepository.deleteAll();
}
    @Test
    @DisplayName("Sucesso ao realizar um pagamento de reserva")
    void Test_payment_success() throws Exception {
  
          paymentDto=new PaymentDto(null, null, PaymentMethodEnum.MONEY, null, null, 0);        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=reservationModel.getId();
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              )
              .andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
              .andDo(MockMvcResultHandlers.print());   
    
          verify(emailService,times(1)).sendEmail(any(EmailModel.class), any(Object.class), anyString());
       
    }
    
  @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva nao confirmada")
    void Test_payment_error_with_reservation_pending() throws Exception{
      reservationModel.setStatus(StatusEnum.PENDING);
      reservationRepository.save(reservationModel);

          paymentDto=new PaymentDto(null, null, PaymentMethodEnum.MONEY, null, null, 0);        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=reservationModel.getId();
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              )
              .andExpect(MockMvcResultMatchers.status().isConflict());
    
          verify(emailService,times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());   
    }

     @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva inexistente")
    void Test_payment_error_with_reservation_notExists() throws Exception{
       paymentDto=new PaymentDto(null, null, PaymentMethodEnum.MONEY, null, null, 0);
        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=99L;
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound())
              .andDo(MockMvcResultHandlers.print());
                    verify(emailService,times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());
     
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