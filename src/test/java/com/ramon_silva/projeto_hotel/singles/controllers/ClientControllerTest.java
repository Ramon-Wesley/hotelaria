package com.ramon_silva.projeto_hotel.singles.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ramon_silva.projeto_hotel.controllers.ClientController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.ramon_silva.projeto_hotel.dto.ClientDto;
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

@BeforeEach
public void setUp(){
}

@Test
@DisplayName("Cliente criado com sucesso")
void Test_create_client_controller_success(){

  clientModel =ClientCreator.createNewClientModel();
  clientDto=new ClientDto(clientModel);
  clientModel.setId(1L);
  clientModel.getAddress().setId(1L);
  ClientDto clientDto2=new ClientDto(clientModel);
  
  when(clientServiceIMP.create(any(ClientDto.class))).thenReturn(clientDto2);

  UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/clientes");
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
  clientDto=ClientCreator.createClientEmailEqualsToBeSaved();  
  when(clientServiceIMP.create(clientDto)).thenThrow(GeralException.class);
  UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/clientes");
  assertThrows(GeralException.class, ()->clientController.create(clientDto, uriBuilder));  
  verify(clientServiceIMP,times(1)).create(clientDto);   
   
}

@Test
@DisplayName("Atualizar clientes")
void Test_update_client_by_id(){
  clientModel=ClientCreator.updateModelClient();
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


}

