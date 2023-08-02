package com.ramon_silva.projeto_hotel.services;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.util.ClientCreator;


@ExtendWith(MockitoExtension.class)
//@SpringBootTest
class ClientServiceIMPTest {

    private ClientDto clientDto;
    private ClientModel clientModel;
    
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceIMP clientServiceIMP;


    @BeforeEach
     void setUp(){  
     clientRepository.save(new ClientModel(1L, ClientCreator.createClientToBeSaved()));
    }

      @Test
      @DisplayName("Salvar cliente com email ja cadastrado")
      void saveEmailExists(){
        clientDto=ClientCreator.createClientEmailEqualsToBeSaved();
        Mockito.when(clientRepository.existsByEmail(clientDto.email())).thenReturn(true);
        Mockito.when(clientRepository.existsByCpf(clientDto.cpf())).thenReturn(false);
        Assertions.assertThrows(GeralException.class,()-> clientServiceIMP.create(clientDto));    
        verify(clientRepository,never()).save(new ClientModel(null, clientDto));
        verify(clientRepository,atMost(1)).existsByEmail(clientDto.email());
        verify(clientRepository,atMost(1)).existsByCpf(clientDto.cpf());
    }

    @Test
    @DisplayName("Salvar cliente com CPF ja cadastrado!")
    void saveCpfExists() {
    clientDto=ClientCreator.createClientCPFEqualsSaved();
    Mockito.when(clientRepository.existsByEmail(clientDto.email())).thenReturn(false);
    Mockito.when(clientRepository.existsByCpf(clientDto.cpf())).thenReturn(true);
    Assertions.assertThrows(GeralException.class,()->clientServiceIMP.create(clientDto));
    verify(clientRepository,never()).save(new ClientModel(null,clientDto));
    verify(clientRepository,atMost(1)).existsByEmail(clientDto.email());
    verify(clientRepository,atMost(1)).existsByCpf(clientDto.cpf());
}

@Test
@DisplayName("Salvar novo cliente!")
void createClient(){
  clientDto = ClientCreator.createNewClient();
  clientModel=new ClientModel(clientDto.id(),clientDto);
  Mockito.when(clientRepository.existsByEmail(clientDto.email())).thenReturn(false);
  Mockito.when(clientRepository.existsByCpf(clientDto.cpf())).thenReturn(false);
  Mockito.when(clientRepository.save(Mockito.any(ClientModel.class))).thenReturn(clientModel);
  ClientDto result=clientServiceIMP.create(clientDto);
  
  verify(clientRepository,times(2)).save(Mockito.any(ClientModel.class));

  assertNotNull(result);
  assertEquals(clientDto.cpf(),result.cpf());
  assertEquals(clientDto.email(),result.email());
 

}

@Test
@DisplayName("Salvar com dados vazios")
void saveEmptyClient(){
    Mockito.when(clientRepository.existsByEmail(clientDto.email())).thenReturn(false);
    Mockito.when(clientRepository.existsByCpf(clientDto.cpf())).thenReturn(true);
   
}

@Test
@DisplayName("Deletar o cliente pelo id")
void testDeleteById() {
    clientDto = ClientCreator.createClientToBeSaved();
    ClientModel savedClient = new ClientModel(clientDto.id(), clientDto);

    Mockito.when(clientRepository.findById(savedClient.getId()))
    .thenReturn(Optional.of(savedClient));

    clientServiceIMP.deleteById(savedClient.getId());
    
    verify(clientRepository, times(1)).deleteById(savedClient.getId());
    verify(clientRepository, times(1)).findById(savedClient.getId());
}

    @Test
    void testGetAll() {

    }

    @Test
    void testGetById() {

    }

    @Test
    void testUpdateById() {

    }
}
