
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
import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.util.ClientCreator;


import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ClientControllerIT {



@Autowired
private MockMvc mockMvc;

@Autowired
private ClientRepository clientRepository;

private ObjectMapper objectJson=new ObjectMapper();

private ClientDto clientDto;

private ClientModel clientModel;

@BeforeEach
public void setUp(){

 clientModel =clientRepository.save(ClientCreator.createClientModelToBeSaved());
 clientRepository.save(ClientCreator.createClientModelToBeSaved2()); 
 clientModel = new ClientModel(clientModel.getId(), clientModel.getName(),
 clientModel.getCpf(), clientModel.getEmail(), clientModel.getPhone(), clientModel.getAddress());

}

@AfterEach
public void setDown(){
  clientRepository.deleteAll();
}


    @Test
    @DisplayName("Rota para salvar um novo cliente!")
    void Test_create_new_client() throws Exception {
        clientDto = ClientCreator.createNewClient();
        String clientJson = objectJson.writeValueAsString(clientDto);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(clientJson))
                 .andExpect(MockMvcResultMatchers.status().isCreated())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                 .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clientDto.name()))  
                 .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(clientDto.email()))  
                
                 .andDo(MockMvcResultHandlers.print());
      
    }


    @Test
    @DisplayName("Tentar criar um cliente com os dados vazios ou nulos")
    void Test_create_new_client_with_dates_null_or_empty() throws Exception {
        clientDto=new ClientDto(null, null, null, null, null, null);
        String clientInvalidJson=objectJson.writeValueAsString(clientDto);
  
        this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
        .contentType(MediaType.APPLICATION_JSON)
        .content(clientInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
            }

    @Test
    @DisplayName("Tentar criar um novo usuario com um email ou um cpf ja cadastrado")
    void Test_create_new_client_with_exists_email_or_with_cpf() throws Exception{
     clientDto=ClientCreator.createClientToBeSaved();
     String clientJson=objectJson.writeValueAsString(clientDto);
   

     this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
     .contentType(MediaType.APPLICATION_JSON)
     .content(clientJson)
     )
     .andExpect(MockMvcResultMatchers.status().isConflict());

   
    }

    @Test
    @DisplayName("Atualizar registro do cliente")
    void Test_update_client_by_id() throws Exception {
        ClientModel client=new ClientModel(
        clientModel.getId(), clientModel.getName(), 
        clientModel.getCpf(), clientModel.getEmail(), 
        clientModel.getPhone(), clientModel.getAddress());

    
        client.setPhone(ClientCreator.createNewClient().phone());
        client.setEmail("emailEmaill@gmail.com");
        ClientDto clientDto2=new ClientDto(client);
        String clientJson=objectJson.writeValueAsString(clientDto2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", clientDto2.id())
        .content(clientJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(clientModel.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.phone").value(Matchers.not(clientModel.getPhone())))   
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(Matchers.not(clientModel.getEmail()))) 
        .andDo(MockMvcResultHandlers.print());  
                
         }

    @Test
    @DisplayName("Atualizar registro com email ou cpf ja cadastrado")
      void Test_update_client_with_exists_email_or_with_cpf() throws Exception{
        clientDto=ClientCreator.createClientToBeSaved();
        ClientModel clientmodel=new ClientModel(clientModel.getId(), clientDto);
        clientmodel.setCpf(ClientCreator.createClientModelToBeSaved2().getCpf());
        clientmodel.setEmail(ClientCreator.createClientModelToBeSaved2().getEmail());

        ClientDto clientDto2=new ClientDto(clientmodel);
        String clientJson=objectJson.writeValueAsString(clientDto2);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", clientDto2.id())
        .content(clientJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isConflict());

      }
      
      @Test
      @DisplayName("Atualizar registro com cliente invalido")
      void Test_update_client_not_existe() throws Exception{
          clientDto=ClientCreator.createClientToBeSaved();
          String clientJson=objectJson.writeValueAsString(clientDto);

          this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}",5L)
          .content(clientJson)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isNotFound());   

      }

    @Test
    @DisplayName("Atualizar registros com os dados vazios ou nulos")
    void Test_update_client_with_dates_null_or_empty() throws Exception {
        clientDto=new ClientDto(clientModel.getId(), null, null, null, null, null);
        String clientInvalidJson=objectJson.writeValueAsString(clientDto);

    
        this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}",clientDto.id())
        .contentType(MediaType.APPLICATION_JSON)
        .content(clientInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
      }

    @Test
    @DisplayName("Deletar registro de cliente")
    void Test_delete_client_by_id() throws Exception{
      Long id= clientModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/{id}",id)
).andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    @DisplayName("Deletar registro inexistente")
    void Test_delete_client_by_id_with_id_nonexistent() throws Exception{
    Long id=99L;

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/{id}",id)
).andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    
    @Test
    @DisplayName("Pegar registro de um cliente pelo id")
    void Test_get_client_by_id() throws Exception{
      
      Long id= clientModel.getId();

      this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}",id )
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(clientModel.getId().intValue()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clientModel.getName()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(clientModel.getEmail()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(clientModel.getCpf()));
      
    }

    @Test
    @DisplayName("Tentar pegar dados de um cliente com id inexistente")
    void Test_get_client_by_nonexist_id() throws Exception{
      Long id= 99L;

      this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", id)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
      
    }
 

@Test
@DisplayName("Pegar uma lista de clientes descendente ordenada pelo id")
void Test_get_all_client_list_by_desc_order() throws Exception{

  int pageNumber= 0;
  int pageSize = 10;

  String sortBy = "id";
  String sortOrder = "desc";

  this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
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
@DisplayName("Pegar uma lista de clientes de forma ascendente ordenada pelo nome")
void Test_get_all_client_list_by_asc_and_order_by_name() throws Exception{

  int pageNumber= 0;
  int pageSize = 10;

  String sortBy = "name";
  String sortOrder = "asc";

  this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
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

