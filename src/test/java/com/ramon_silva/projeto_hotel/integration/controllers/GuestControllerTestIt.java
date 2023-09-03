
package com.ramon_silva.projeto_hotel.integration.controllers;


import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.repositories.GuestRepository;
import com.ramon_silva.projeto_hotel.util.GuestCreator;


import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@Transactional
@ActiveProfiles("test")
public class GuestControllerTestIt {



@Autowired
private MockMvc mockMvc;

@Autowired
private GuestRepository guestRepository;

private ObjectMapper objectJson=new ObjectMapper();

private GuestDto guestDto;

private GuestModel guestModel;

@Autowired
private ModelMapper modelMapper;

@BeforeEach
public void setUp(){
  
 guestModel =guestRepository.save(GuestCreator.newGuestModel()); 
 guestRepository.save(GuestCreator.newGuestModel3());
 guestModel = new GuestModel(guestModel.getId(), guestModel.getName(),
 guestModel.getCpf(), guestModel.getEmail(), guestModel.getPhone(), guestModel.getAddress());

}

@AfterEach
public void setDown(){
  guestRepository.deleteAll();
}


    @Test
    @DisplayName("Rota para salvar um novo hospede!")
    void Test_create_new_guest() throws Exception {
        guestDto=modelMapper.map(GuestCreator.newGuestModel2(),GuestDto.class);
        String guestJson = objectJson.writeValueAsString(guestDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/hospedes")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(guestJson))
                 .andExpect(MockMvcResultMatchers.status().isCreated())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(guestDto.getName()))  
                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(guestDto.getEmail()))  
                 .andDo(MockMvcResultHandlers.print());
      
    }


    @Test
    @DisplayName("Tentar criar um hospede com os dados vazios ou nulos")
    void Test_create_new_guest_with_dates_null_or_empty() throws Exception {
        guestDto=new GuestDto(null, null, null, null, null, null);
        String guestInvalidJson=objectJson.writeValueAsString(guestDto);
  
        this.mockMvc.perform(MockMvcRequestBuilders.post("/hospedes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(guestInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
            }

    @Test
    @DisplayName("Tentar criar um novo usuario com um email ou um cpf ja cadastrado")
    void Test_create_new_guest_with_exists_email_or_with_cpf() throws Exception{
     guestDto=modelMapper.map(GuestCreator.newGuestModel(),GuestDto.class);
     String guestJson=objectJson.writeValueAsString(guestDto);
   

     this.mockMvc.perform(MockMvcRequestBuilders.post("/hospedes")
     .contentType(MediaType.APPLICATION_JSON)
     .content(guestJson)
     )
     .andExpect(MockMvcResultMatchers.status().isConflict());

   
    }

    @Test
    @DisplayName("Atualizar registro do hospede")
    void Test_update_guest_by_id() throws Exception {
        GuestModel guest=new GuestModel(
        guestModel.getId(), guestModel.getName(), 
        guestModel.getCpf(), guestModel.getEmail(), 
        guestModel.getPhone(), guestModel.getAddress()
        );

    
        guest.setPhone(GuestCreator.newGuestModel2().getPhone());
        guest.setEmail("emailEmaill@gmail.com");

        GuestDto guestDto2=modelMapper.map(guest,GuestDto.class);
        String guestJson=objectJson.writeValueAsString(guestDto2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/hospedes/{id}", guestDto2.getId())
        .content(guestJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(guestModel.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(Matchers.not(guestModel.getPhone())))   
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.not(guestModel.getEmail()))) 
        .andDo(MockMvcResultHandlers.print());  
                
         }

    @Test
    @DisplayName("Atualizar registro com email ou cpf ja cadastrado")
      void Test_update_guest_with_exists_email_or_with_cpf() throws Exception{
        
       
        guestModel.setCpf(GuestCreator.newGuestModel3().getCpf());
        guestModel.setEmail(GuestCreator.newGuestModel3().getEmail());

        GuestDto guestDto2=modelMapper.map(guestModel,GuestDto.class);
        String guestJson=objectJson.writeValueAsString(guestDto2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/hospedes/{id}", guestDto2.getId())
        .content(guestJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isConflict());

      }
      
      @Test
      @DisplayName("Atualizar registro com hospede invalido")
      void Test_update_guest_not_existe() throws Exception{
        Long id=99L;
        GuestModel guestModel=GuestCreator.newGuestModel2();
        guestModel.setId(3L);
        guestModel.getAddress().setId(3L);
          guestDto=modelMapper.map(guestModel,GuestDto.class);
          
          String guestJson=objectJson.writeValueAsString(guestDto);

          this.mockMvc.perform(MockMvcRequestBuilders.put("/hospedes/{id}",id)
          .content(guestJson)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isNotFound());   

      }

    @Test
    @DisplayName("Atualizar registros com os dados vazios ou nulos")
    void Test_update_guest_with_dates_null_or_empty() throws Exception {
        guestDto=new GuestDto(guestModel.getId(), null, null, null, null, null);
        String guestInvalidJson=objectJson.writeValueAsString(guestDto);

    
        this.mockMvc.perform(MockMvcRequestBuilders.put("/hospedes/{id}",guestDto.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(guestInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
      }

    @Test
    @DisplayName("Deletar registro de hospede")
    void Test_delete_guest_by_id() throws Exception{
      Long id= guestModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/hospedes/{id}",id)
).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("Deletar registro inexistente")
    void Test_delete_guest_by_id_with_id_nonexistent() throws Exception{
    Long id=99L;

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/hospedes/{id}",id)
).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    @DisplayName("Pegar registro de um hospede pelo id")
    void Test_get_guest_by_id() throws Exception{
      
      Long id= guestModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.get("/hospedes/{id}",id )
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(guestModel.getId().intValue()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(guestModel.getName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(guestModel.getEmail()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(guestModel.getCpf()));
      
    }

    @Test
    @DisplayName("Tentar pegar dados de um hospede com id inexistente")
    void Test_get_guest_by_nonexist_id() throws Exception{
      Long id= 99L;

      this.mockMvc.perform(MockMvcRequestBuilders.get("/hospedes/{id}", id)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
      
    }
 

@Test
@DisplayName("Pegar uma lista de hospedes descendente ordenada pelo id")
void Test_get_all_guest_list_by_desc_order() throws Exception{

  int pageNumber= 0;
  int pageSize = 10;

  String sortBy = "id";
  String sortOrder = "desc";

  this.mockMvc.perform(MockMvcRequestBuilders.get("/hospedes")
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
@DisplayName("Pegar uma lista de hospedes de forma ascendente ordenada pelo nome")
void Test_get_all_guest_list_by_asc_and_order_by_name() throws Exception{

  int pageNumber= 0;
  int pageSize = 10;

  String sortBy = "name";
  String sortOrder = "asc";

  this.mockMvc.perform(MockMvcRequestBuilders.get("/hospedes")
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

