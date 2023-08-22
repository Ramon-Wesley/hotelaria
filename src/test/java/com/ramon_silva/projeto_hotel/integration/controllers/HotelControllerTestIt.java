
package com.ramon_silva.projeto_hotel.integration.controllers;


import org.hamcrest.Matchers;
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
import com.ramon_silva.projeto_hotel.dto.HotelDto;
import com.ramon_silva.projeto_hotel.models.HotelModel;

import com.ramon_silva.projeto_hotel.repositories.HotelRepository;
import com.ramon_silva.projeto_hotel.util.HotelCreator;


import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
public class HotelControllerTestIt {



@Autowired
private MockMvc mockMvc;

@Autowired
private HotelRepository hotelRepository;

private ObjectMapper objectJson=new ObjectMapper();

private HotelDto hotelDto;

private HotelModel hotelModel;

@BeforeEach
public void setUp(){
  
 hotelModel =hotelRepository.save(HotelCreator.newModelHotel()); 
 hotelRepository.save(HotelCreator.newModelHotel3());
 hotelModel = new HotelModel(hotelModel.getId(), hotelModel.getName(), hotelModel.getCnpj(),
  hotelModel.getEmail(), hotelModel.getPhone(), hotelModel.getDescription(), hotelModel.getClassification(),
   hotelModel.getRooms(), hotelModel.getAddress());

}

@AfterEach
public void setDown(){
  hotelRepository.deleteAll();
}


    @Test
    @DisplayName("Rota para salvar um novo HOTEL!")
    void Test_create_new_hotel() throws Exception {
        hotelDto=new HotelDto(HotelCreator.newModelHotel2());
        String hotelJson = objectJson.writeValueAsString(hotelDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/hotel")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(hotelJson))
                 .andExpect(MockMvcResultMatchers.status().isCreated())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(hotelDto.name()))  
                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(hotelDto.email()))  
                 .andDo(MockMvcResultHandlers.print());
      
    }


    @Test
    @DisplayName("Tentar criar um hotel com os dados vazios ou nulos")
    void Test_create_new_client_with_dates_null_or_empty() throws Exception {
        hotelDto=new HotelDto(null, null, null, null, null, null, 0, null);
        String hotelInvalidJson=objectJson.writeValueAsString(hotelDto);
  
        this.mockMvc.perform(MockMvcRequestBuilders.post("/hotel")
        .contentType(MediaType.APPLICATION_JSON)
        .content(hotelInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
            }

    @Test
    @DisplayName("Tentar criar um novo hotel com um email ou um cnpj ja cadastrado")
    void Test_create_new_hotel_with_exists_email_or_with_cnpj() throws Exception{
     hotelDto=new HotelDto(HotelCreator.newModelHotel());
     String hotelJson=objectJson.writeValueAsString(hotelDto);
   

     this.mockMvc.perform(MockMvcRequestBuilders.post("/hotel")
     .contentType(MediaType.APPLICATION_JSON)
     .content(hotelJson)
     )
     .andExpect(MockMvcResultMatchers.status().isConflict());

   
    }

    @Test
    @DisplayName("Atualizar registro do hotel")
    void Test_update_hotel_by_id() throws Exception {
        HotelModel hotel = new HotelModel(hotelModel.getId(), hotelModel.getName(), hotelModel.getCnpj(),
        hotelModel.getEmail(), hotelModel.getPhone(), hotelModel.getDescription(), hotelModel.getClassification(),
         hotelModel.getRooms(), hotelModel.getAddress());

    
        hotelModel.setPhone(HotelCreator.newModelHotel2().getPhone());
        hotelModel.setEmail("emailEmaill@gmail.com");

        HotelDto hotelDto2=new HotelDto(hotel);
        String hotelJson=objectJson.writeValueAsString(hotelDto2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/hotel/{id}", hotelDto2.id())
        .content(hotelJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(hotelModel.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(Matchers.not(hotelModel.getPhone())))   
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.not(hotelModel.getEmail()))) 
        .andDo(MockMvcResultHandlers.print());  
                
         }

    @Test
    @DisplayName("Atualizar registro com email ou cnpj ja cadastrado")
      void Test_update_hotel_with_exists_email_or_with_cnpj() throws Exception{
        
       
        hotelModel.setCnpj(HotelCreator.newModelHotel3().getCnpj());
        hotelModel.setEmail(HotelCreator.newModelHotel3().getEmail());

        HotelDto hotelDto2=new HotelDto(hotelModel);
        String hotelJson=objectJson.writeValueAsString(hotelDto2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/hotel/{id}", hotelDto2.id())
        .content(hotelJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isConflict());

      }
      
      @Test
      @DisplayName("Atualizar registro com hotel invalido")
      void Test_update_hotel_not_existe() throws Exception{
        Long id=99L;
        HotelModel hotelModel=HotelCreator.newModelHotel2();
        hotelModel.setId(3L);
        hotelModel.getAddress().setId(3L);
          hotelDto=new HotelDto(hotelModel);
          
          String hotelJson=objectJson.writeValueAsString(hotelDto);

          this.mockMvc.perform(MockMvcRequestBuilders.put("/hotel/{id}",id)
          .content(hotelJson)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isNotFound());   

      }

    @Test
    @DisplayName("Atualizar registros com os dados vazios ou nulos")
    void Test_update_hotel_with_dates_null_or_empty() throws Exception {
        hotelDto=new HotelDto(hotelModel.getId(), null, null, null, null, null, 0, null);
        String hotelInvalidJson=objectJson.writeValueAsString(hotelDto);

    
        this.mockMvc.perform(MockMvcRequestBuilders.put("/hotel/{id}",hotelDto.id())
        .contentType(MediaType.APPLICATION_JSON)
        .content(hotelInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
      }

    @Test
    @DisplayName("Deletar registro de hotel")
    void Test_delete_hotel_by_id() throws Exception{
      Long id= hotelModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/hotel/{id}",id)
).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("Deletar registro inexistente")
    void Test_delete_hotel_by_id_with_id_nonexistent() throws Exception{
    Long id=99L;

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/hotel/{id}",id)
).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    @DisplayName("Pegar registro de um hotel pelo id")
    void Test_get_hotel_by_id() throws Exception{
      
      Long id= hotelModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.get("/hotel/{id}",id )
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(hotelModel.getId().intValue()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(hotelModel.getName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(hotelModel.getEmail()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.cnpj").value(hotelModel.getCnpj()));
      
    }

    @Test
    @DisplayName("Tentar pegar dados de um hotel com id inexistente")
    void Test_get_hotel_by_nonexist_id() throws Exception{
      Long id= 99L;

      this.mockMvc.perform(MockMvcRequestBuilders.get("/hotel/{id}", id)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
      
    }
 

@Test
@DisplayName("Pegar uma lista de hotel descendente ordenada pelo id")
void Test_get_all_hotel_list_by_desc_order() throws Exception{

  int pageNumber= 0;
  int pageSize = 10;

  String sortBy = "id";
  String sortOrder = "desc";

  this.mockMvc.perform(MockMvcRequestBuilders.get("/hotel")
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
@DisplayName("Pegar uma lista de hoteis de forma ascendente ordenada pelo nome")
void Test_get_all_hotel_list_by_asc_and_order_by_name() throws Exception{

  int pageNumber= 0;
  int pageSize = 10;

  String sortBy = "name";
  String sortOrder = "asc";

  this.mockMvc.perform(MockMvcRequestBuilders.get("/hotel")
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


}

