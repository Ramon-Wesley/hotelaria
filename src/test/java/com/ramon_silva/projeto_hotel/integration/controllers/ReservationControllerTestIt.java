package com.ramon_silva.projeto_hotel.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
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
import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDatesDto;
import com.ramon_silva.projeto_hotel.dto.ReservationDto;
import com.ramon_silva.projeto_hotel.dto.Reservation_serviceDto;
import com.ramon_silva.projeto_hotel.dto.ServicesDto;
import com.ramon_silva.projeto_hotel.enums.StatusEnum;
import com.ramon_silva.projeto_hotel.models.AddressModel;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.models.EmailModel;
import com.ramon_silva.projeto_hotel.models.HotelModel;
import com.ramon_silva.projeto_hotel.models.ReservationModel;
import com.ramon_silva.projeto_hotel.models.Reservation_serviceModel;
import com.ramon_silva.projeto_hotel.models.RoomModel;
import com.ramon_silva.projeto_hotel.models.ServicesModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.repositories.ReservationRepository;
import com.ramon_silva.projeto_hotel.repositories.RoomRepository;
import com.ramon_silva.projeto_hotel.repositories.ServicesRepository;
import com.ramon_silva.projeto_hotel.services.EmailServiceIMP;
import com.ramon_silva.projeto_hotel.util.AddressCreator;
import com.ramon_silva.projeto_hotel.util.GuestCreator;
import com.ramon_silva.projeto_hotel.util.HotelCreator;
import com.ramon_silva.projeto_hotel.util.ReservationCreator;
import com.ramon_silva.projeto_hotel.util.RoomCreator;
import com.ramon_silva.projeto_hotel.util.ServiceCreator;

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
   private GuestRepository guestRepository;

   @Autowired
   private RoomRepository roomRepository;

   @Autowired
   private HotelRepository hotelRepository;

   @Autowired
   private ServicesRepository servicesRepository;

   @MockBean
   private EmailServiceIMP mockEmailService;

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ModelMapper modelMapper = new ModelMapper();

   private ReservationModel reservationModel;
   private ReservationDto reservationDto;
   private GuestModel guestModel;
   private AddressModel addressModel;
   private HotelModel hotelModel;
   private RoomModel roomModel;

   private List<ServicesModel> servicesModel = new ArrayList<>();
   private List<ServicesDto> servicesDto = new ArrayList<>();
   private Set<Reservation_serviceModel> reservation_serviceModels = new HashSet<>();
   private ReservationModel reservationModelResult;

   private ObjectMapper objectMapper = new ObjectMapper();

   @BeforeEach
   @Transactional
   void setUp() {
      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      SaveReservation(reservationRepository, guestRepository, roomRepository, hotelRepository);
      SaveDates(guestRepository, roomRepository, hotelRepository);

      reservationModelResult = new ReservationModel(null, guestModel,
            roomModel,
            LocalDate.now().plusYears(1), LocalDate.now().plusYears(1).plusDays(2), null, null, null);
      servicesDto = new ArrayList<>();
   }

   @AfterEach
   @Transactional
   void setDown() {
      reservation_serviceModels = new HashSet<>();
      reservationRepository.deleteAll();
   }

   @Test
   @DisplayName("Erro ao salvar uma reserva com datas conflitantes em um mesmo quarto")
   void Test_create_error_reservation_room_with_dates_conflict() throws Exception {
      reservationDto = modelMapper.map(reservationModel, ReservationDto.class);
      reservationDto.setId(null);
      String json = objectMapper.writeValueAsString(reservationDto);
      mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andDo(MockMvcResultHandlers.log())
            .andExpect(MockMvcResultMatchers.status().isConflict());

      verify(emailService, times(0)).sendEmail(any(EmailModel.class), any(Object.class), anyString());

   }

   @Test
   @DisplayName("Salvar uma reserva com sucesso")
   void Test_create_reservation_success() throws Exception {
      reservationDto = modelMapper.map(reservationModelResult, ReservationDto.class);
      reservationDto.setStatus(StatusEnum.PENDING);
      String json = objectMapper.writeValueAsString(reservationDto);
      System.out.println(json);
      mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

      verify(emailService, times(1)).sendEmail(any(EmailModel.class), any(Object.class), anyString());

   }

   @Test
   @DisplayName("Erro ao salvar uma reserva com as datas invalidas!")
   void Test_create_reservation_error_with_dates_empty() throws Exception {
      reservationDto = modelMapper.map(reservationModelResult, ReservationDto.class);
      reservationDto.setCheckInDate(LocalDate.now().minusWeeks(2));
      reservationDto.setCheckOutDate(LocalDate.now().minusWeeks(1));

      String json = objectMapper.writeValueAsString(reservationDto);
      mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andDo(MockMvcResultHandlers.log())
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
   }

   @Test
   @DisplayName("Criar uma reserva com um hospede inexistente")
   void Test_create_reservation_with_guest_id_not_exists() throws Exception {
      reservationDto = modelMapper.map(reservationModelResult, ReservationDto.class);
      reservationDto.getGuest().setId(99L);
      reservationDto.setStatus(StatusEnum.PENDING);
      String json = objectMapper.writeValueAsString(reservationDto);
      System.out.println(json);
      mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andDo(MockMvcResultHandlers.log())
            .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @DisplayName("Criar uma reserva com um quarto inexistente")
   void Test_create_reservation_with_room_id_not_exists() throws Exception {
      reservationDto = modelMapper.map(reservationModelResult, ReservationDto.class);
      reservationDto.getRoom().setId(99L);
      reservationDto.setStatus(StatusEnum.PENDING);

      String json = objectMapper.writeValueAsString(reservationDto);
      mockMvc.perform(MockMvcRequestBuilders.post("/reserva")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andDo(MockMvcResultHandlers.log())
            .andExpect(MockMvcResultMatchers.status().isNotFound());

   }

   @Test
   @DisplayName("Encontrar uma reserva pelo id")
   void Test_success_get_by_id_reservation() throws Exception {

      long id_reservation = reservationModel.getId();
      mockMvc.perform(MockMvcRequestBuilders.get("/reserva/{id_reserva}", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());
   }

   @Test
   @DisplayName("Reserva com id inexistente")
   void Test_get_by_id_with_reservation_id_not_exists() throws Exception {

      long id_reservation = 99L;
      mockMvc.perform(MockMvcRequestBuilders.get("/reserva/{id_reserva}", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @DisplayName("Atualizar registro com sucesso")
   void Test_success_update_by_id() throws Exception {

      reservationDto = modelMapper.map(reservationModel, ReservationDto.class);
      reservationDto.setGuest(modelMapper.map(guestModel, GuestDto.class));
      String json = objectMapper.writeValueAsString(reservationDto);
      Long id_reservation = reservationDto.getId();
      mockMvc.perform(MockMvcRequestBuilders.put("/reserva/{id_reserva}", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();
   }

   @Test
   @DisplayName("Atualizar registro com id inexistente")
   void Test_error_update_with_id_not_exists() throws Exception {

      reservationDto = modelMapper.map(reservationModel, ReservationDto.class);
      reservationDto.setGuest(modelMapper.map(guestModel, GuestDto.class));
      String json = objectMapper.writeValueAsString(reservationDto);
      Long id_reservation = 99L;
      System.out.println(json);
      mockMvc.perform(MockMvcRequestBuilders.put("/reserva/{id_reserva}", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(MockMvcResultMatchers.status().isNotFound())
            .andReturn();
   }

   @Test
   @DisplayName("Atualizar registro com  dados nulos")
   void Test_error_update_with_dates_null() throws Exception {

      reservationDto = new ReservationDto(reservationModel.getId(), null, null, null, null, null, null, null);
      String json = objectMapper.writeValueAsString(reservationDto);
      Long id_reservation = 99L;
      mockMvc.perform(MockMvcRequestBuilders.put("/reserva/{id_reserva}", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn();
   }

   @Test
   @DisplayName("Adicionar servicos com sucesso!")
   void Test_success_add_services() throws Exception {

      Long id_reservation = reservationModel.getId();
      servicesModel.stream().forEach(
            (e) -> servicesDto.add(modelMapper.map(e, ServicesDto.class)));

      String json = objectMapper.writeValueAsString(servicesDto);
      System.out.println("SERVICOS PRESTADOS AGORA: " + json);
      mockMvc.perform(MockMvcRequestBuilders.post("/reserva/{id_reserva}/servicos", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(MockMvcResultMatchers.status().isOk());
   }

   @Test
   @DisplayName("Adicionar servicos com reserva inexistente!")
   void Test_add_services_with_reservation_not_exists() throws Exception {
      Long id_reservation = 99L;
      servicesModel.stream().forEach((e) -> servicesDto.add(modelMapper.map(e, ServicesDto.class)));

      String json = objectMapper.writeValueAsString(servicesDto);

      mockMvc.perform(MockMvcRequestBuilders.post("/reserva/{id_reserva}/servicos", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @DisplayName("Adicionar servicos inexistentes na reserva!")
   void Test_add_services_not_exists() throws Exception {
      Long id_reservation = reservationModel.getId();
      servicesModel.stream().forEach((e) -> servicesDto.add(modelMapper.map(e, ServicesDto.class)));
      ServicesDto serviceDto = new ServicesDto(99L, null, null, null, null);
      servicesDto.add(serviceDto);
      String json = objectMapper.writeValueAsString(servicesDto);

      mockMvc.perform(MockMvcRequestBuilders.post("/reserva/{id_reserva}/servicos", id_reservation)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json)).andExpect(MockMvcResultMatchers.status().isConflict());
   }

   @Test
   @DisplayName("deletar servicos na reserva!")
   void Test_delete_by_id_reservation_services() throws Exception {
      servicesModel.stream().forEach((e) -> reservation_serviceModels
            .add(new Reservation_serviceModel(reservationModel, e)));
      reservationModel.setReservation_service(reservation_serviceModels);
      reservationRepository.save(reservationModel);
      Long id_reservation = reservationModel.getId();
      Long id_service = reservationModel.getReservation_service().iterator().next().getId();
      mockMvc.perform(
            MockMvcRequestBuilders.delete("/reserva/{id_reserva}/servicos/{id_servico}", id_reservation, id_service)
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk());
   }

   @Test
   @DisplayName("deletar servicos inexistente!")
   void Test_delete_by_id_reservation_services_not_exists() throws Exception {
      Long id_reservation = reservationModel.getId();
      long id_service = 99L;
      mockMvc.perform(
            MockMvcRequestBuilders.delete("/reserva/{id_reserva}/servicos/{id_servico}", id_reservation, id_service)
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @DisplayName("deletar servicos de uma reserva inexistente!")
   void Test_delete_by_id_reservation_not_exists() throws Exception {
      Long id_reservation = 99L;
      long id_service = servicesModel.get(0).getId();
      mockMvc.perform(
            MockMvcRequestBuilders.delete("/reserva/{id_reserva}/servicos/{id_servico}", id_reservation, id_service)
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound());
   }

   @Test
   @DisplayName("Listar todas as reservas")
   void Test_get_all_reservations() throws Exception {
      int pageNumber = 0;
      int pageSize = 5;
      String sortBy = "id";
      String sortOrder = "asc";

      this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva")
            .param("pageNumber", String.valueOf(pageNumber))
            .param("pageSize", String.valueOf(pageSize))
            .param("sortBy", sortBy)
            .param("sortOrder", sortOrder)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.getContent").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageNumber))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(pageSize))
            .andDo(MockMvcResultHandlers.print());
   }

   @Test
   @DisplayName("Listar todos os servicos de uma reserva")
   void Test_get_all_services_reservation() throws Exception {
      long id_reservation = reservationModel.getId();
      servicesModel.stream().forEach((services) -> reservation_serviceModels.add(
            new Reservation_serviceModel(reservationModel, services)));
      reservationModel.setReservation_service(reservation_serviceModels);
      reservationRepository.save(reservationModel);

      this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva/{id_reserva}/servicos", id_reservation)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());

   }

   @Test
   @DisplayName("Listar servicos inexistentes de uma reserva")
   void Test_get_all_services_reservation_not_exists() throws Exception {
      long id_reservation = reservationModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva/{id_reserva}/servicos", id_reservation)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isNotFound())
            .andDo(MockMvcResultHandlers.print());

   }

   @DisplayName("Listar todos os servicos que estao vinculados a todas as reservas")
   void Test_get_all_services_reservations() throws Exception {
      int pageNumber = 0;
      int pageSize = 5;
      String sortBy = "id";
      String sortOrder = "asc";

      this.mockMvc.perform(MockMvcRequestBuilders.get("/reserva/servicos")
            .param("pageNumber", String.valueOf(pageNumber))
            .param("pageSize", String.valueOf(pageSize))
            .param("sortBy", sortBy)
            .param("sortOrder", sortOrder)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.getContent").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.pageNumber").value(pageNumber))
            .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(pageSize))
            .andDo(MockMvcResultHandlers.print());

   }

   @Test
   @DisplayName("Listar um servico de uma reserva ")
   void Test_get_by_id__services_reservation() throws Exception {
      servicesModel.stream().forEach((services) -> reservation_serviceModels.add(
            new Reservation_serviceModel(reservationModel, services)));
      reservationModel.setReservation_service(reservation_serviceModels);
      reservationRepository.save(reservationModel);
      long id_reservation = reservationModel.getId();
      long id_service_reservation = reservationModel.getReservation_service()
            .iterator().next().getId();

      this.mockMvc
            .perform(MockMvcRequestBuilders
                  .get("/reserva/{id_reserva}/servico/{id_servico}", id_reservation, id_service_reservation)
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());

   }

   public void SaveReservation(

         ReservationRepository reservationRepository,

         GuestRepository guestRepository,

         RoomRepository roomRepository,

         HotelRepository hotelRepository) {

      addressModel = AddressCreator.newAddressModel();

      guestModel = new GuestModel();
      guestModel = GuestCreator.newGuestModel();
      guestModel.setAddress(addressModel);

      addressModel = AddressCreator.newAddressModel2();

      hotelModel = HotelCreator.newModelHotel();
      hotelModel.setAddress(addressModel);

      guestModel = guestRepository.save(guestModel);
      hotelModel = hotelRepository.save(hotelModel);

      roomModel = RoomCreator.newModelRoom();
      roomModel.setHotel(hotelModel);

      roomModel = roomRepository.save(roomModel);

      servicesModel = servicesRepository.saveAll(ServiceCreator.getServices());

      reservationModel = ReservationCreator.newReservationModel();
      reservationModel.setGuest(guestModel);
      reservationModel.setRoom(roomModel);
      reservationModel.setReservation_service(reservation_serviceModels);
      reservationModel = reservationRepository.save(reservationModel);

   }

   public void SaveDates(

         GuestRepository guestRepository,

         RoomRepository roomRepository,

         HotelRepository hotelRepository) {

      addressModel = AddressCreator.newAddressModel();

      guestModel = new GuestModel();
      guestModel = GuestCreator.newGuestModel2();
      guestModel.setAddress(addressModel);

      addressModel = AddressCreator.newAddressModel3();

      hotelModel = HotelCreator.newModelHotel2();
      hotelModel.setAddress(addressModel);

      guestModel = guestRepository.save(guestModel);
      hotelModel = hotelRepository.save(hotelModel);

      roomModel = RoomCreator.newModelRoom2();
      roomModel.setHotel(hotelModel);

      roomModel = roomRepository.save(roomModel);

   }

}
