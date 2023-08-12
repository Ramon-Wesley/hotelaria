package com.ramon_silva.projeto_hotel.singles.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.ramon_silva.projeto_hotel.dto.ClientDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;
import com.ramon_silva.projeto_hotel.models.ClientModel;
import com.ramon_silva.projeto_hotel.repositories.ClientRepository;
import com.ramon_silva.projeto_hotel.services.ClientServiceIMP;
import com.ramon_silva.projeto_hotel.util.ClientCreator;

@ExtendWith(MockitoExtension.class)
class ClientServiceIMPTest {

    private ClientDto clientDto;
    private ClientModel clientModel;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceIMP clientServiceIMP;

    @Test
    @DisplayName("Salvar cliente com email ou cpf ja cadastrado")
    void Test_saving_lient_with_existing_email() {
        clientModel =ClientCreator.newClientModel();
   
        clientDto = new ClientDto(clientModel);

        when(clientRepository.existsByEmail(clientDto.email())).thenReturn(true);
        when(clientRepository.existsByCpf(clientDto.cpf())).thenReturn(true);

        assertThrows(GeralException.class, () -> clientServiceIMP.create(clientDto), "Email ja cadastrado!");

        verify(clientRepository, never()).save(clientModel);
        verify(clientRepository, times(1)).existsByEmail(clientDto.email());
        verify(clientRepository, times(1)).existsByCpf(clientDto.cpf());
    }

    @Test
    @DisplayName("Salvar novo cliente!")
    void Test_create_new_client() {
        clientModel = ClientCreator.newClientModel();
        clientDto=new ClientDto(clientModel);
        clientModel.setId(1L);
        clientModel.getAddress().setId(1L);

        when(clientRepository.existsByEmail(clientDto.email())).thenReturn(false);
        when(clientRepository.existsByCpf(clientDto.cpf())).thenReturn(false);

        when(clientRepository.save(any(ClientModel.class))).thenReturn(clientModel);
        ClientDto result = clientServiceIMP.create(clientDto);

        verify(clientRepository,times(1)).existsByEmail(anyString());
        verify(clientRepository,times(1)).existsByCpf(anyString());
        verify(clientRepository,times(1)).save(Mockito.any(ClientModel.class));
       
        assertNotNull(result.id());
        assertNotNull(result.address().id());
        

    }

    @Test
    @DisplayName("Salvar dados vazios")
    void Test_creating_client_with_empty_data() {
        Assertions.assertThrows(NullPointerException.class, () -> clientServiceIMP.create(null),
                "Cliente não pode ser nulo");
                verify(clientRepository,never()).save(any(ClientModel.class));
                verify(clientRepository,never()).existsByEmail(anyString());
                verify(clientRepository,never()).existsByCpf(anyString());
    }

    @Test
    @DisplayName("Deletar o cliente pelo id!")
    void Test_deleting_client_by_id() {
        
        clientModel = ClientCreator.newClientModel();
        clientModel.setId(1L);
        clientModel.getAddress().setId(1L);

        Mockito.when(clientRepository.findById(clientModel.getId()))
                .thenReturn(Optional.of(clientModel));

        clientServiceIMP.deleteById(clientModel.getId());

        verify(clientRepository, times(1)).deleteById(clientModel.getId());
        verify(clientRepository, times(1)).findById(clientModel.getId());
    }

    @Test
    @DisplayName("Deletar o cliente com um id inválido!")
    void Test_deleting_client_with_invalid_ID() {
Long id=99L;
        when(clientRepository.findById(id))
                .thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> clientServiceIMP.deleteById(id),
                "Id inválido");

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, never()).deleteById(id);

    }

    @Test
    @DisplayName("Listar todos os clientes")
    void Test_getting_all_clients() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        ClientModel  client1 = ClientCreator.newClientModel();
        client1.setId(1L);
        client1.getAddress().setId(1L);

        ClientModel client2 = ClientCreator.newClientModel();
        client2.setId(2L);
        client2.getAddress().setId(2L);

        List<ClientModel> clients = Arrays.asList(client1, client2);

        Page<ClientModel> page = new PageImpl<>(clients);

        when(clientRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageDto<ClientDto> result = clientServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);

        verify(clientRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));

        assertEquals(2, result.totalElments());
        assertEquals(1, result.totalPages());

    }

    @Test
    @DisplayName("Listar dados vazios")
    void Test_getting_all_empty_clients() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";

        List<ClientModel> client = Arrays.asList();
        Page<ClientModel> page = new PageImpl<>(client);
        when(clientRepository.findAll(any(Pageable.class))).thenReturn(page);
        PageDto<ClientDto> result = clientServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder);
        verify(clientRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending()));

        assertEquals(0, result.totalElments());
        assertEquals(1, result.totalPages());
    }

    @Test
    @DisplayName("Pegar um usuario pelo id")
    void Test_getting_by_id_client() {

        clientModel = ClientCreator.newClientModel();
        clientModel.setId(1L);
        clientModel.getAddress().setId(1L);
       

        when(clientRepository.findById(clientModel.getId())).thenReturn(Optional.of(clientModel));

        ClientDto clientDto2 = clientServiceIMP.getById(clientModel.getId());

        verify(clientRepository, times(1)).findById(clientModel.getId());
        assertEquals(clientModel.getId(), clientDto2.id(), "Os dados nao sao compativeis");

    }

    @Test
    @DisplayName("Tentar pegar um usuario que nao existe pelo id")
    void Test_getting_by_id_not_found_client() {

        Long id=99L;
        when(clientRepository.findById(id)).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> clientServiceIMP.getById(id),
                "Cliente nao encontrado!");

        verify(clientRepository, times(1)).findById(id);

    }

    @Test
    @DisplayName("Atualizar registro")
    void Test_update_by_id() {
        clientModel = ClientCreator.newClientModel();
        clientModel.setId(1L);
        clientModel.getAddress().setId(1L);

       clientDto=new ClientDto(clientModel);

        ClientDto clientDto2 = new ClientDto(clientDto.id(),
                clientDto.name(),
                ClientCreator.newClientModel2().getCpf(),
                ClientCreator.newClientModel2().getEmail(),
                 clientDto.phone(),
                clientDto.address());

        ClientModel clientModel2 = new ClientModel(clientDto2.id(), clientDto2);

        when(clientRepository.findById(clientDto.id())).thenReturn(Optional.of(clientModel));
        when(clientRepository.save(Mockito.any(ClientModel.class))).thenReturn(clientModel2);

        ClientDto resulDto = clientServiceIMP.updateById(clientDto.id(), clientDto2);

        verify(clientRepository, times(1)).findById(clientDto.id());
        verify(clientRepository, times(1)).save(Mockito.any(ClientModel.class));

        assertNotNull(resulDto);
        assertNotEquals(clientDto, resulDto, "Os dados do cliente são iguais!");
        assertEquals(clientDto.id(), resulDto.id(), "O id do cliente nao é igual! ");

    }

    @Test
    @DisplayName("Atualizar registro com id invalido")
    void Test_update_by_invalid_id() {
        Long id =99L;
        clientModel = ClientCreator.newClientModel();
        clientModel.setId(1L);
        clientModel.getAddress().setId(1L);

        clientDto=new ClientDto(clientModel);
        when(clientRepository.findById(id)).thenThrow(ResourceNotFoundException.class);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> clientServiceIMP.updateById(id, clientDto),
                "Id invalido");

        verify(clientRepository, times(1)).findById(id);
        verify(clientRepository, times(0)).save(Mockito.any(ClientModel.class));

    }

    @Test
    @DisplayName("Atualizar registros sem os dados do cliente")
    void Test_update_by_id_with_null_client() {
        Long id=1L;
        Assertions.assertThrows(NullPointerException.class, () -> clientServiceIMP.updateById(id, null),
                "dados do cliente sao nulos");
        verify(clientRepository, times(0)).findById(id);
        verify(clientRepository, times(0)).save(Mockito.any(ClientModel.class));

    }
}
