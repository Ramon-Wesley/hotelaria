package com.ramon_silva.projeto_hotel.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ramon_silva.projeto_hotel.dto.PaymentDto;
import com.ramon_silva.projeto_hotel.enums.PaymentMethodEnum;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.PaymentModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.PaymentRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.services.EmailServiceIMP;
import com.ramon_silva.projeto_hotel.util.AddressCreator;
import com.ramon_silva.projeto_hotel.util.GuestCreator;
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
    private ReservationRepository reservationRepository;

    @Autowired
    private EmailServiceIMP emailService;
    
    @Autowired
    private GuestRepository guestRepository;
   
    @Autowired
    private RoomRepository roomRepository;
   
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private EmailServiceIMP mockEmailService; 

    @Autowired
    private MockMvc mockMvc;

   
    private static boolean dataInitialize=false;
    private PaymentModel paymentModel;

    private PaymentDto paymentDto;

    private ReservationModel reservationModel;
    private GuestModel guestModel;
    private AddressModel addressModel;
    private HotelModel hotelModel;
    private RoomModel roomModel;
    private Set<Reservation_serviceModel> services=new HashSet<>();

    private ReservationModel reservationModelPayment;

    private ObjectMapper objectMapper=new ObjectMapper();
    
@BeforeAll
@Transactional
void setUp(

){
  if(!dataInitialize){
    objectMapper.registerModule(new JavaTimeModule()); 
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    SaveReservation(reservationRepository, guestRepository, roomRepository, hotelRepository);
    
    dataInitialize=true;
  }

}
@AfterEach
void setUpEach(){
  paymentRepository.deleteAll();
}

@BeforeEach
void setDown(){
  SavePayment(reservationRepository,paymentRepository,guestRepository, hotelRepository,roomRepository);
}

    @Test
    @DisplayName("Sucesso ao realizar um pagamento de reserva")
    void Test_payment_success() throws Exception {
  
          paymentDto=new PaymentDto
          (null, null, 
          PaymentMethodEnum.MONEY,
           null, null, 0);        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=reservationModel.getId();
          System.out.println(json);
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/reserva/{reservation_id}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              )
              .andExpect(MockMvcResultMatchers.status().isCreated())
              .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
            
    
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
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/reserva/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              )
              .andExpect(MockMvcResultMatchers.status().isConflict());
    
          verify(emailService,times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());   
    }
    @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva ja paga")
    void Test_payment_error_with_reservation_pay() throws Exception{
     
          paymentDto=new PaymentDto(null, null, PaymentMethodEnum.MONEY, null, null, 0);        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=reservationModelPayment.getId();
          mockMvc.perform(MockMvcRequestBuilders.post("/pagamento/reserva/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json)
              )
              .andExpect(MockMvcResultMatchers.status().isConflict());
           
    
          verify(emailService,times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());   
    }

     @Test
    @DisplayName("erro ao realizar um pagamento com uma reserva inexistente")
    void Test_payment_error_with_reservation_notExists() throws Exception{
       paymentDto=new PaymentDto(null, null, 
       PaymentMethodEnum.MONEY,
        null, null, 0);
        
          String json=objectMapper.writeValueAsString(paymentDto);
          Long reservation_id=99L;
          mockMvc.perform(MockMvcRequestBuilders
          .post("/pagamento/reserva/{id_reservation}", reservation_id)
              .contentType(MediaType.APPLICATION_JSON)
              .content(json))
              .andExpect(MockMvcResultMatchers.status().isNotFound());     
           verify(emailService,times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());
     
    }

    @Test
    @DisplayName("Deletar com sucesso um pagamento")
    void Test_delete_payment_by_id() throws Exception{
         Long payment_id=paymentModel.getId();

          mockMvc.perform(MockMvcRequestBuilders.delete("/pagamento/{id}", payment_id))
          .andExpect(MockMvcResultMatchers.status().isOk());


    }
    
    @Test
    @DisplayName("Deletar um pagamento inexistente")
    void Test_delete_payment_by_id_notExists ()throws Exception{
      Long payment_id=99L;

      mockMvc.perform(MockMvcRequestBuilders.delete("/pagamento/{id}", payment_id))
      .andExpect(MockMvcResultMatchers.status().isNotFound());

      
    }
    @Test
    @DisplayName("Atualizar pagamento existente")
    void Test_update_payment_by_id() throws Exception{
      paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
   
      
      String json=objectMapper.writeValueAsString(paymentDto);
      long id=paymentDto.getId();
           mockMvc.perform(MockMvcRequestBuilders.put("/pagamento/{id}",id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            ).andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    @DisplayName("Atualizar pagamento inexistente")
    void Test_update_payment_with_notExist_id() throws Exception{
      long id=99;
      paymentDto=modelMapper.map(paymentModel,PaymentDto.class);
    
           String json=objectMapper.writeValueAsString(paymentDto);
           mockMvc.perform(MockMvcRequestBuilders.put("/pagamento/{id}",id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)
            ).andExpect(MockMvcResultMatchers.status().isNotFound());
              
    }

  @Test
    @DisplayName("selecionar com sucesso um pagamento")
    void Test_get_payment_by_id() throws Exception{
       long id=paymentModel.getId();

       mockMvc.perform(MockMvcRequestBuilders.get("/pagamento/{id}", id)
       .contentType(MediaType.APPLICATION_JSON))
       .andExpect(MockMvcResultMatchers.status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

      @Test
    @DisplayName("selecionar um pagamento inexistente")
    void Test_get_payment_notExist_by_id() throws Exception{
   long id=99L;

       mockMvc.perform(MockMvcRequestBuilders.get("/pagamento/{id}", id)
       .contentType(MediaType.APPLICATION_JSON))
       .andExpect(MockMvcResultMatchers.status().isNotFound());
       
    }


    @Test
    @DisplayName("Listar pagamentos")
    void Test_get_all_payment() throws Exception{
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        String sortOrder = "asc";
  


  this.mockMvc.perform(MockMvcRequestBuilders.get("/pagamento")
  .param("pageNumber", String.valueOf(pageNumber))
  .param("pageSize", String.valueOf(pageSize))
  .param("sortBy", sortBy)
  .param("sortOrder", sortOrder)
  .contentType(MediaType.APPLICATION_JSON)
  )
  .andExpect(MockMvcResultMatchers.status().isOk())
  .andExpect(MockMvcResultMatchers.jsonPath("$.getContent").exists())
  .andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageNumber))
  .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(pageSize))
  .andDo(MockMvcResultHandlers.print());
      }

     @Test
    @DisplayName("Listar pagamentos com lista vazia")
    void Test_get_all_payment_empty_list() throws Exception{
      paymentRepository.deleteAll();  

        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        String sortOrder = "asc";
        

     
  this.mockMvc.perform(MockMvcRequestBuilders.get("/pagamento")
  .param("pageNumber", String.valueOf(pageNumber))
  .param("pageSize", String.valueOf(pageSize))
  .param("sortBy", sortBy)
  .param("sortOrder", sortOrder)
  .contentType(MediaType.APPLICATION_JSON)
  )
  .andExpect(MockMvcResultMatchers.status().isOk())
  .andExpect(MockMvcResultMatchers.jsonPath("$.getContent").exists())
  .andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageNumber))
  .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(pageSize))
  .andDo(MockMvcResultHandlers.print());
     }

     public void SaveReservation(
    
      ReservationRepository reservationRepository,
  
       GuestRepository guestRepository,
     
       RoomRepository roomRepository,
     
       HotelRepository hotelRepository
     ){
       
  addressModel=AddressCreator.newAddressModel();
    
  guestModel=new GuestModel();
  guestModel=GuestCreator.newGuestModel();
  guestModel.setAddress(addressModel);

  
  addressModel=AddressCreator.newAddressModel2();
  
  hotelModel=HotelCreator.newModelHotel();
  hotelModel.setAddress(addressModel);

  
  guestModel=guestRepository.save(guestModel);
  hotelModel=hotelRepository.save(hotelModel);

  roomModel=RoomCreator.newModelRoom();
  roomModel.setHotel(hotelModel);

  roomModel=roomRepository.save(roomModel);

  reservationModel=ReservationCreator.newReservationModel();
  reservationModel.setGuest(guestModel);
  reservationModel.setRoom(roomModel);
  reservationModel.setStatus(StatusEnum.CONFIRM);
  reservationModel.setReservation_service(services);
  reservationModel=reservationRepository.save(reservationModel);


     }

     public void SavePayment(
       ReservationRepository reservationRepository,
       
       PaymentRepository paymentRepository2,
       
       GuestRepository guestRepository,
     
       HotelRepository hotelRepository,

       RoomRepository roomRepository
     
           ){
       
  addressModel=AddressCreator.newAddressModel2();
    
  guestModel=new GuestModel();
  guestModel=GuestCreator.newGuestModel2();
  guestModel.setAddress(addressModel);

  
  addressModel=AddressCreator.newAddressModel3();
  
  hotelModel=HotelCreator.newModelHotel2();
  hotelModel.setAddress(addressModel);

  
  guestModel=guestRepository.save(guestModel);
  hotelModel=hotelRepository.save(hotelModel);

  roomModel=RoomCreator.newModelRoom2();
  roomModel.setHotel(hotelModel);

  roomModel=roomRepository.save(roomModel);

  reservationModelPayment=ReservationCreator.newReservationModel2();
  reservationModelPayment.setGuest(guestModel);
  reservationModelPayment.setRoom(roomModel);
  reservationModelPayment.setStatus(StatusEnum.CONFIRM);
  reservationModelPayment.setReservation_service(services);
  reservationModelPayment=reservationRepository.save(reservationModelPayment);

  Double valueService=reservationModelPayment.getReservation_service().stream()
  .mapToDouble(res->res.getServico().getPrice()).sum();

  paymentModel=new PaymentModel();
  paymentModel.setReservation(reservationModelPayment);
  paymentModel.setPaymentMethod( PaymentMethodEnum.CREDIT);
  paymentModel.setStatus(StatusEnum.CONFIRM);
  paymentModel.setTotal_payment(reservationModelPayment.getTotal_pay()+valueService);
  paymentModel=paymentRepository2.save(paymentModel);

     }

}
