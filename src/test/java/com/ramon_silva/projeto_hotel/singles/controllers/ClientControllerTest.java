package com.ramon_silva.projeto_hotel.singles.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;

import org.mockito.junit.jupiter.MockitoExtension;
import com.ramon_silva.projeto_hotel.controllers.ClientController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.services.ClientServiceIMP;
import com.ramon_silva.projeto_hotel.util.ClientCreator;


@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

@InjectMocks
private ClientController clientController;

@Mock
private ClientServiceIMP clientServiceIMP;

private ClientDto clientDto;

private ClientModel clientModel;

private UriComponentsBuilder uriBuilder=UriComponentsBuilder.newInstance();
@BeforeEach
public void setUp(){
}

@Test
@DisplayName("Cliente criado com sucesso")
void Test_create_client_controller_success(){

  clientModel =ClientCreator.newClientModel();
  clientDto=new ClientDto(clientModel);
  clientModel.setId(1L);
  clientModel.getAddress().setId(1L);
  ClientDto clientDto2=new ClientDto(clientModel);
  
  when(clientServiceIMP.create(any(ClientDto.class))).thenReturn(clientDto2);

        ResponseEntity<ClientDto> response = clientController.create(clientDto, uriBuilder);

        verify(clientServiceIMP,times(1)).create(any(ClientDto.class));
         assertNotNull(response.getBody());
         assertNotNull(response.getBody().id());
         assertNotNull(response.getBody().address().id());
         assertEquals(response.getStatusCode(),HttpStatus.CREATED);
}

@Test
@DisplayName("Criar cliente com cpf ou email iguais ")
void Test_create_client_with_dates_empty(){
  clientModel =ClientCreator.newClientModel();
  clientDto=new ClientDto(clientModel);


  when(clientServiceIMP.create(clientDto)).thenThrow(GeralException.class);
   assertThrows(GeralException.class, ()->clientController.create(clientDto, uriBuilder));  
  verify(clientServiceIMP,times(1)).create(clientDto);   
   
}

@Test
@DisplayName("Atualizar clientes")
void Test_update_client_by_id(){
  clientModel =ClientCreator.newClientModel();
  clientModel.setId(1L);
  clientModel.getAddress().setId(1L);

  clientDto=new ClientDto(clientModel);

  clientModel.setName("mudados");
  clientModel.setPhone("(11)11111-11111");
  
  ClientDto clientDto2=new ClientDto(clientModel);

  
  when(clientServiceIMP.updateById(eq(clientDto.id()), any(ClientDto.class))).thenReturn(clientDto2);

  ResponseEntity<ClientDto> response=clientController.updateById(clientDto.id(), clientDto2);
    
  verify(clientServiceIMP, times(1)).updateById(eq(clientDto.id()), any(ClientDto.class));

  assertEquals(HttpStatus.OK, response.getStatusCode());
  assertEquals(clientDto.id(), response.getBody().id());
  assertNotEquals(clientDto.phone(), response.getBody().phone());
  assertNotEquals(clientDto.name(), response.getBody().name());

}

@Test
@DisplayName("Atualizar clientes com cpf ou email ja cadastrados")
void Test_update_client_by_id_with_cpf_or_email_exists(){
   clientModel =ClientCreator.newClientModel();
   clientModel.setId(1L);
   clientModel.getAddress().setId(1L);

  clientDto=new ClientDto(clientModel);
  when(clientServiceIMP.updateById(eq(clientDto.id()), any(ClientDto.class))).thenThrow(GeralException.class);
  assertThrows(GeralException.class,()->clientController.updateById(clientDto.id(), clientDto));    
  verify(clientServiceIMP, times(1)).updateById(eq(clientDto.id()), any(ClientDto.class));

}

@Test
@DisplayName("Atualizar clientes com id inexistente")
void Test_update_client_by_nonexist_id(){
  Long id=99l;
   clientModel =ClientCreator.newClientModel();
   clientModel.setId(1L);
   clientModel.getAddress().setId(1L);
  clientDto=new ClientDto(clientModel);
  when(clientServiceIMP.updateById(eq(id), any(ClientDto.class))).thenThrow(ResourceNotFoundException.class);
  assertThrows(ResourceNotFoundException.class,()->clientController.updateById(id, clientDto));    
  verify(clientServiceIMP, times(1)).updateById(eq(id), any(ClientDto.class));

}

@Test
@DisplayName("Deletar cliente com sucesso")
void Test_delete_client_by_id(){
  Long id=1L;

  doNothing().when(clientServiceIMP).deleteById(id);
  ResponseEntity<Void> response=clientController.deleteById(id);
  verify(clientServiceIMP,times(1)).deleteById(id);
  assertEquals(HttpStatus.OK,response.getStatusCode());
  assertNull(response.getBody());
}

@Test
@DisplayName("Deletar cliente com id inexistente")
void Test_delete_client_by_nonexist_id(){
  Long id=99L;

  doThrow(ResourceNotFoundException.class).when(clientServiceIMP).deleteById(eq(id));

  assertThrows(ResourceNotFoundException.class,()->clientController.deleteById(id));

  verify(clientServiceIMP, times(1)).deleteById(eq(id));

}

@Test
@DisplayName("encontrar cliente pelo id com sucesso")
void Test_get_client_by_id(){
  
  clientModel =ClientCreator.newClientModel();
   clientModel.setId(1L);
   clientModel.getAddress().setId(1L);
  clientDto=new ClientDto(clientModel);

  when(clientServiceIMP.getById(clientDto.id())).thenReturn(clientDto);

  ResponseEntity<ClientDto> response=clientController.getById(clientDto.id());
  
  verify(clientServiceIMP,times(1)).getById(clientDto.id());
  
  assertEquals(HttpStatus.OK,response.getStatusCode());
  assertNotNull(response.getBody());
}

@Test
@DisplayName("encontrar cliente com id inexistente")
void Test_get_client_by_nonexist_id(){
  Long id=99L;

  doThrow(ResourceNotFoundException.class).when(clientServiceIMP).getById(eq(id));

  assertThrows(ResourceNotFoundException.class,()->clientController.getById(id));

  verify(clientServiceIMP, times(1)).getById(eq(id));

}

@Test
@DisplayName("Listar todos os clientes")
void Test_get_all_clients(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
        int numberOfElements= 2;
        int totalPages= 1;
        long totalElments=2;

   clientModel =ClientCreator.newClientModel();
   clientModel.setId(1L);
   clientModel.getAddress().setId(1L);

   ClientModel clientModel2 =ClientCreator.newClientModel2();
   clientModel2.setId(2L);
   clientModel2.getAddress().setId(2L);

        ClientDto clientDto=new ClientDto(clientModel);
        ClientDto clientDto2=new ClientDto(clientModel2);

        List<ClientDto> clientDtos=new ArrayList<>();
        clientDtos.add(clientDto);
        clientDtos.add(clientDto2);

        PageDto<ClientDto> pageDto= new PageDto<>(clientDtos, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(clientServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(pageDto);

        ResponseEntity<PageDto<ClientDto>> response=clientController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(clientDtos.size(),response.getBody().getContent().size());
        assertEquals(clientDtos.size(),response.getBody().numberOfElements() );
        
}

@Test
@DisplayName("Listar todos os clientes com lista vazia")
void Test_get_all_clients_empty_list(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
        int numberOfElements= 0;
        int totalPages= 1;
        long totalElments=0;


   
        List<ClientDto> clientDtos=new ArrayList<>();

        PageDto<ClientDto> pageDto= new PageDto<>(clientDtos, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(clientServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(pageDto);

        ResponseEntity<PageDto<ClientDto>> response=clientController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(clientDtos.size(),response.getBody().getContent().size());
        assertEquals(clientDtos.size(),response.getBody().numberOfElements() );
        
}
}

