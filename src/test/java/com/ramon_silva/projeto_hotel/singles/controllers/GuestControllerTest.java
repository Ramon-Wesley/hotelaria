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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import com.ramon_silva.projeto_hotel.infra.errors.GeralException;
import com.ramon_silva.projeto_hotel.infra.errors.ResourceNotFoundException;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import com.ramon_silva.projeto_hotel.controllers.GuestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.ramon_silva.projeto_hotel.dto.GuestDto;
import com.ramon_silva.projeto_hotel.dto.PageDto;
import com.ramon_silva.projeto_hotel.models.GuestModel;
import com.ramon_silva.projeto_hotel.services.GuestServiceIMP;
import com.ramon_silva.projeto_hotel.util.GuestCreator;


@ExtendWith(MockitoExtension.class)
public class GuestControllerTest {

@InjectMocks
private GuestController guestController;

@Autowired
private ModelMapper modelMapper=new ModelMapper();

@Mock
private GuestServiceIMP guestServiceIMP;

private GuestDto guestDto;

private GuestModel guestModel;

private UriComponentsBuilder uriBuilder=UriComponentsBuilder.newInstance();
@BeforeEach
public void setUp(){
}

@Test
@DisplayName("Gueste criado com sucesso")
void Test_create_guest_controller_success(){

  guestModel =GuestCreator.newGuestModel();
  guestDto=modelMapper.map(guestModel,GuestDto.class);
  guestModel.setId(1L);
  guestModel.getAddress().setId(1L);
  GuestDto guestDto2=modelMapper.map(guestModel,GuestDto.class);;
  
  when(guestServiceIMP.create(any(GuestDto.class))).thenReturn(guestDto2);

        ResponseEntity<GuestDto> response = guestController.create(guestDto, uriBuilder);

        verify(guestServiceIMP,times(1)).create(any(GuestDto.class));
         assertNotNull(response.getBody());
         assertNotNull(response.getBody().getId());
         assertNotNull(response.getBody().getAddress().getId());
         assertEquals(response.getStatusCode(),HttpStatus.CREATED);
}

@Test
@DisplayName("Criar hospede com cpf ou email iguais ")
void Test_create_guest_with_dates_empty(){
  guestModel =GuestCreator.newGuestModel();
  guestDto=modelMapper.map(guestModel,GuestDto.class);


  when(guestServiceIMP.create(guestDto)).thenThrow(GeralException.class);
   assertThrows(GeralException.class, ()->guestController.create(guestDto, uriBuilder));  
  verify(guestServiceIMP,times(1)).create(guestDto);   
   
}

@Test
@DisplayName("Atualizar hospedes")
void Test_update_guest_by_id(){
  guestModel =GuestCreator.newGuestModel();
  guestModel.setId(1L);
  guestModel.getAddress().setId(1L);

  guestDto=modelMapper.map(guestModel,GuestDto.class);

  guestModel.setName("mudados");
  guestModel.setPhone("(11)11111-11111");
  
  GuestDto guestDto2=modelMapper.map(guestModel,GuestDto.class);

  
  when(guestServiceIMP.updateById(eq(guestDto.getId()), any(GuestDto.class))).thenReturn(guestDto2);

  ResponseEntity<GuestDto> response=guestController.updateById(guestDto.getId(), guestDto2);
    
  verify(guestServiceIMP, times(1)).updateById(eq(guestDto.getId()), any(GuestDto.class));

  assertEquals(HttpStatus.OK, response.getStatusCode());
  assertEquals(guestDto.getId(), response.getBody().getId());
  assertNotEquals(guestDto.getPhone(), response.getBody().getPhone());
  assertNotEquals(guestDto.getName(), response.getBody().getName());

}

@Test
@DisplayName("Atualizar hospedes com cpf ou email ja cadastrados")
void Test_update_guest_by_id_with_cpf_or_email_exists(){
   guestModel =GuestCreator.newGuestModel();
   guestModel.setId(1L);
   guestModel.getAddress().setId(1L);

  guestDto=modelMapper.map(guestModel,GuestDto.class);
  when(guestServiceIMP.updateById(eq(guestDto.getId()), any(GuestDto.class))).thenThrow(GeralException.class);
  assertThrows(GeralException.class,()->guestController.updateById(guestDto.getId(), guestDto));    
  verify(guestServiceIMP, times(1)).updateById(eq(guestDto.getId()), any(GuestDto.class));

}

@Test
@DisplayName("Atualizar hospedes com id inexistente")
void Test_update_guest_by_nonexist_id(){
  Long id=99l;
   guestModel =GuestCreator.newGuestModel();
   guestModel.setId(1L);
   guestModel.getAddress().setId(1L);
  guestDto=modelMapper.map(guestModel,GuestDto.class);
  when(guestServiceIMP.updateById(eq(id), any(GuestDto.class))).thenThrow(ResourceNotFoundException.class);
  assertThrows(ResourceNotFoundException.class,()->guestController.updateById(id, guestDto));    
  verify(guestServiceIMP, times(1)).updateById(eq(id), any(GuestDto.class));

}

@Test
@DisplayName("Deletar hospede com sucesso")
void Test_delete_guest_by_id(){
  Long id=1L;

  doNothing().when(guestServiceIMP).deleteById(id);
  ResponseEntity<Void> response=guestController.deleteById(id);
  verify(guestServiceIMP,times(1)).deleteById(id);
  assertEquals(HttpStatus.OK,response.getStatusCode());
  assertNull(response.getBody());
}

@Test
@DisplayName("Deletar hospede com id inexistente")
void Test_delete_guest_by_nonexist_id(){
  Long id=99L;

  doThrow(ResourceNotFoundException.class).when(guestServiceIMP).deleteById(eq(id));

  assertThrows(ResourceNotFoundException.class,()->guestController.deleteById(id));

  verify(guestServiceIMP, times(1)).deleteById(eq(id));

}

@Test
@DisplayName("encontrar hospede pelo id com sucesso")
void Test_get_guest_by_id(){
  
  guestModel =GuestCreator.newGuestModel();
   guestModel.setId(1L);
   guestModel.getAddress().setId(1L);
  guestDto=modelMapper.map(guestModel,GuestDto.class);

  when(guestServiceIMP.getById(guestDto.getId())).thenReturn(guestDto);

  ResponseEntity<GuestDto> response=guestController.getById(guestDto.getId());
  
  verify(guestServiceIMP,times(1)).getById(guestDto.getId());
  
  assertEquals(HttpStatus.OK,response.getStatusCode());
  assertNotNull(response.getBody());
}

@Test
@DisplayName("encontrar hospede com id inexistente")
void Test_get_guest_by_nonexist_id(){
  Long id=99L;

  doThrow(ResourceNotFoundException.class).when(guestServiceIMP).getById(eq(id));

  assertThrows(ResourceNotFoundException.class,()->guestController.getById(id));

  verify(guestServiceIMP, times(1)).getById(eq(id));

}

@Test
@DisplayName("Listar todos os hospedes")
void Test_get_all_guests(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
        int numberOfElements= 2;
        int totalPages= 1;
        long totalElments=2;

   guestModel =GuestCreator.newGuestModel();
   guestModel.setId(1L);
   guestModel.getAddress().setId(1L);

   GuestModel guestModel2 =GuestCreator.newGuestModel2();
   guestModel2.setId(2L);
   guestModel2.getAddress().setId(2L);

        GuestDto guestDto=modelMapper.map(guestModel,GuestDto.class);
        GuestDto guestDto2=modelMapper.map(guestModel2,GuestDto.class);

        List<GuestDto> guestDtos=new ArrayList<>();
        guestDtos.add(guestDto);
        guestDtos.add(guestDto2);

        PageDto<GuestDto> pageDto= new PageDto<>(guestDtos, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(guestServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(pageDto);

        ResponseEntity<PageDto<GuestDto>> response=guestController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(guestDtos.size(),response.getBody().getGetContent().size());
        assertEquals(guestDtos.size(),response.getBody().getNumberOfElements() );
        
}

@Test
@DisplayName("Listar todos os hospedes com lista vazia")
void Test_get_all_guests_empty_list(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        String sortOrder = "asc";
        int numberOfElements= 0;
        int totalPages= 1;
        long totalElments=0;


   
        List<GuestDto> guestDtos=new ArrayList<>();

        PageDto<GuestDto> pageDto= new PageDto<>(guestDtos, pageNumber, numberOfElements, pageSize, totalPages, totalElments);
        when(guestServiceIMP.getAll(pageNumber, pageSize, sortBy, sortOrder)).thenReturn(pageDto);

        ResponseEntity<PageDto<GuestDto>> response=guestController.getAll(pageNumber, pageSize, sortBy, sortOrder);

        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(guestDtos.size(),response.getBody().getGetContent().size());
        assertEquals(guestDtos.size(),response.getBody().getNumberOfElements() );
        
}

}

