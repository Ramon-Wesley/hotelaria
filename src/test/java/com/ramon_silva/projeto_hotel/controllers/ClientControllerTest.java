package com.ramon_silva.projeto_hotel.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.services.ClientServiceIMP;
import com.ramon_silva.projeto_hotel.util.ClientCreator;


import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class ClientControllerTest {


  @MockBean
private ClientServiceIMP clientServiceIMP;

@Autowired
private MockMvc mockMvc;

private ObjectMapper objectJson=new ObjectMapper();

private ClientDto clientDto;


@BeforeEach
public void setUp(){
}



    @Test
    @DisplayName("Rota para salvar um novo cliente!")
    void Test_create_new_client() throws Exception {
       clientDto = ClientCreator.createNewClient();
        String clientJson = objectJson.writeValueAsString(clientDto);

          when(clientServiceIMP.create(any(ClientDto.class))).thenReturn(clientDto);
    
           ResultActions resultApi = this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(clientJson))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.content().json(clientJson));
           ClientDto resultDto=objectJson.readValue(resultApi.andReturn().getResponse().getContentAsString(),ClientDto.class);

           assertNotNull(resultDto);
            verify(clientServiceIMP,times(1)).create(any(ClientDto.class));

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
        
        verify(clientServiceIMP,times(0)).create(any(ClientDto.class));
    }

    @Test
    @DisplayName("Tentar criar um novo usuario com um email ou um cpf ja cadastrado")
    void Test_create_new_client_with_exists_email_or_with_cpf() throws Exception{
     clientDto=ClientCreator.createClientToBeSaved();
     String clientJson=objectJson.writeValueAsString(clientDto);
     when(clientServiceIMP.create(any(ClientDto.class))).thenThrow(GeralException.class);

     this.mockMvc.perform(MockMvcRequestBuilders.post("/clientes")
     .content(clientJson)
     .contentType(MediaType.APPLICATION_JSON)
     )
     .andExpect(MockMvcResultMatchers.status().isConflict());

    verify(clientServiceIMP,times(1)).create(any(ClientDto.class));
   
    }

    @Test
    @DisplayName("Atualizar registro do cliente")
    void Test_update_client_by_id() throws Exception {
        clientDto=ClientCreator.createClientToBeSaved();
        ClientModel clientmodel=new ClientModel(clientDto.id(), clientDto);
        clientmodel.setPhone(ClientCreator.createNewClient().phone());
        clientmodel.getAddress().setZapCode(ClientCreator.createNewClient().address().zapCode());
        ClientDto clientDto2=new ClientDto(clientmodel);
        String clientJson=objectJson.writeValueAsString(clientDto2);

        when(clientServiceIMP.updateById(eq(clientDto2.id()), any(ClientDto.class))).thenReturn(clientDto2);

         ResultActions resultApi=this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", clientDto2.id())
        .content(clientJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(clientJson));

        ClientDto resultDto=objectJson.readValue(
          resultApi.andReturn().getResponse().getContentAsString(),ClientDto.class
        );
               
        assertEquals(clientDto2.id(), resultDto.id(),"Os objetos devem ter o mesmo id");
        verify(clientServiceIMP,times(1)).updateById(eq(clientDto2.id()), any(ClientDto.class));
    }

    @Test
    @DisplayName("Atualizar registro com email ou cpf ja cadastrado")
      void Test_update_client_with_exists_email_or_with_cpf() throws Exception{
        clientDto=ClientCreator.createClientToBeSaved();
        ClientModel clientmodel=new ClientModel(clientDto.id(), clientDto);
        clientmodel.setCpf(ClientCreator.createClientCPFEqualsSaved().cpf());
        clientmodel.setEmail(ClientCreator.createClientEmailEqualsToBeSaved().email());

        ClientDto clientDto2=new ClientDto(clientmodel);
        String clientJson=objectJson.writeValueAsString(clientDto2);

        when(clientServiceIMP.updateById(eq(clientDto2.id()), any(ClientDto.class))).thenThrow(GeralException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}", clientDto2.id())
        .content(clientJson)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isConflict());

       
        verify(clientServiceIMP,times(1)).updateById(eq(clientDto2.id()), any(ClientDto.class));
  
      }
      @Test
      @DisplayName("Atualizar registro com cliente invalido")
      void Test_update_client_not_existe() throws Exception{
          clientDto=ClientCreator.createClientToBeSaved();
          String clientJson=objectJson.writeValueAsString(clientDto);

          when(clientServiceIMP.updateById(eq(5L), any(ClientDto.class))).thenThrow(ResourceNotFoundException.class);

          this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}",5L)
          .content(clientJson)
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(MockMvcResultMatchers.status().isNotFound());   

         verify(clientServiceIMP,times(1)).updateById(eq(5L), any(ClientDto.class));
}

    @Test
    @DisplayName("Atualizar registros com os dados vazios ou nulos")
    void Test_update_client_with_dates_null_or_empty() throws Exception {
        clientDto=new ClientDto(1L, null, null, null, null, null);
        String clientInvalidJson=objectJson.writeValueAsString(clientDto);

    
        this.mockMvc.perform(MockMvcRequestBuilders.put("/clientes/{id}",clientDto.id())
        .contentType(MediaType.APPLICATION_JSON)
        .content(clientInvalidJson))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
        
        verify(clientServiceIMP,times(0)).updateById(eq(clientDto.id()), any(ClientDto.class));
    }

    @Test
    @DisplayName("Deletar registro de cliente")
    void Test_delete_client_by_id() throws Exception{
      Long id=1L;

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/{id}",id)
).andExpect(MockMvcResultMatchers.status().isOk());
verify(clientServiceIMP,times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Deletar registro inexistente")
    void Test_delete_client_by_id_with_id_nonexistent() throws Exception{
    Long id=99L;

   doThrow(new ResourceNotFoundException("cliente", "id", id))
            .when(clientServiceIMP).deleteById(id);

      this.mockMvc.perform(MockMvcRequestBuilders.delete("/clientes/{id}",id)
).andExpect(MockMvcResultMatchers.status().isNotFound());
verify(clientServiceIMP,times(1)).deleteById(id);
    }
    
    @Test
    @DisplayName("Pegar registro de um cliente pelo id")
    void Test_get_client_by_id() throws Exception{
      Long id= 1L;
      clientDto=ClientCreator.createClientToBeSaved();
      when(clientServiceIMP.getById(id)).thenReturn(clientDto);

      this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", id)
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(clientDto.name()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(clientDto.email()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.cpf").value(clientDto.cpf()));
      verify(clientServiceIMP,times(1)).getById(id);
    }

    @Test
    @DisplayName("Tentar pegar dados de um cliente com id inexistente")
    void Test_get_client_by_nonexist_id() throws Exception{
      Long id= 99L;

      when(clientServiceIMP.getById(id)).thenThrow(ResourceNotFoundException.class);

      this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes/{id}", id)
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound());
      verify(clientServiceIMP,times(1)).getById(id);

    }
 

@Test
@DisplayName("Pegar uma lista de clientes descendente ordenada pelo id")
void Test_get_all_client_list_by_desc_order() throws Exception{
  int numberOfElements = 2;
  Long totalElments = 2L;

  int pageNumber= 0;
  int pageSize = 10;
  int totalPages = 1;

  String sortBy = "id";
  String sortOrder = "desc";

  ClientDto clientDto1=ClientCreator.createClientToBeSaved();
  ClientDto clientDto2=ClientCreator.createToBeSave2();
  List<ClientDto> clientDtos=Arrays.asList(clientDto1,clientDto2);
  
  List<ClientDto> clientDtos2 = new ArrayList<>(clientDtos);
  Collections.sort(clientDtos2,Comparator.comparingLong(ClientDto::id).reversed());

  PageDto<ClientDto> result=new PageDto<>(clientDtos2,pageNumber,numberOfElements,pageSize,totalPages,totalElments);
  
  
   when(clientServiceIMP.getAll(anyInt(), anyInt(), anyString(), anyString())).thenReturn(result);

  this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
  .param("pageNumber", String.valueOf(pageNumber))
  .param("pageSize", String.valueOf(pageSize))
  .param("sortBy", sortBy)
  .param("sortOrder", sortOrder)
  .contentType(MediaType.APPLICATION_JSON)
  )
  .andExpect(MockMvcResultMatchers.status().isOk())
  .andExpect(jsonPath("$.getContent", hasSize(clientDtos.size())))
  .andExpect(jsonPath("$.getContent[0].id", is(clientDto2.id().intValue())))
  .andExpect(jsonPath("$.getContent[0].name", is(clientDto2.name())))
  .andExpect(jsonPath("$.getContent[0].cpf", is(clientDto2.cpf())))
  .andExpect(jsonPath("$.getContent[0].email", is(clientDto2.email())))
  .andExpect(jsonPath("$.getContent[0].phone", is(clientDto2.phone())))
  .andExpect(jsonPath("$.getContent[0].address").exists())
  .andExpect(jsonPath("$.getContent[1].id", is(clientDto1.id().intValue())))
  .andExpect(jsonPath("$.getContent[1].name", is(clientDto1.name())))
  .andExpect(jsonPath("$.getContent[1].cpf", is(clientDto1.cpf())))
  .andExpect(jsonPath("$.getContent[1].email", is(clientDto1.email())))
  .andExpect(jsonPath("$.getContent[1].phone", is(clientDto1.phone())))
  .andExpect(jsonPath("$.getContent[1].address").exists());



  verify(clientServiceIMP,times(1)).getAll(pageNumber, pageSize, sortBy, sortOrder);
  
}


@Test
@DisplayName("Pegar uma lista de clientes de forma ascendente ordenada pelo nome")
void Test_get_all_client_list_by_asc_and_order_by_name() throws Exception{
  int numberOfElements = 2;
  Long totalElments = 2L;

  int pageNumber= 0;
  int pageSize = 10;
  int totalPages = 1;

  String sortBy = "name";
  String sortOrder = "asc";

  ClientDto clientDto1=ClientCreator.createClientToBeSaved();
  ClientDto clientDto2=ClientCreator.createToBeSave2();
  List<ClientDto> clientDtos=Arrays.asList(clientDto1,clientDto2);
  
  List<ClientDto> clientDtos2 = new ArrayList<>(clientDtos);
  Collections.sort(clientDtos2,Comparator.comparing(ClientDto::name));

  PageDto<ClientDto> result=new PageDto<>(clientDtos2,pageNumber,numberOfElements,pageSize,totalPages,totalElments);
  
  
   when(clientServiceIMP.getAll(anyInt(), anyInt(), anyString(), anyString())).thenReturn(result);

  this.mockMvc.perform(MockMvcRequestBuilders.get("/clientes")
  .param("pageNumber", String.valueOf(pageNumber))
  .param("pageSize", String.valueOf(pageSize))
  .param("sortBy", sortBy)
  .param("sortOrder", sortOrder)
  .contentType(MediaType.APPLICATION_JSON)
  )
  .andExpect(MockMvcResultMatchers.status().isOk())
  .andExpect(jsonPath("$.getContent", hasSize(clientDtos.size())))
  .andExpect(jsonPath("$.getContent[0].id", is(clientDto2.id().intValue())))
  .andExpect(jsonPath("$.getContent[0].name", is(clientDto2.name())))
  .andExpect(jsonPath("$.getContent[0].cpf", is(clientDto2.cpf())))
  .andExpect(jsonPath("$.getContent[0].email", is(clientDto2.email())))
  .andExpect(jsonPath("$.getContent[0].phone", is(clientDto2.phone())))
  .andExpect(jsonPath("$.getContent[0].address").exists())
  .andExpect(jsonPath("$.getContent[1].id", is(clientDto1.id().intValue())))
  .andExpect(jsonPath("$.getContent[1].name", is(clientDto1.name())))
  .andExpect(jsonPath("$.getContent[1].cpf", is(clientDto1.cpf())))
  .andExpect(jsonPath("$.getContent[1].email", is(clientDto1.email())))
  .andExpect(jsonPath("$.getContent[1].phone", is(clientDto1.phone())))
  .andExpect(jsonPath("$.getContent[1].address").exists());



  verify(clientServiceIMP,times(1)).getAll(pageNumber, pageSize, sortBy, sortOrder);
  
}


}

